/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.security

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.FastCollectedLruCache
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.persistence.entity.RsaKeyPair
import com.github.fj.restapi.persistence.repository.RsaKeyStoreRepository
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * Does a little memory caching and DB lookup. Since generating and parsing RSA KeyPairs are quite
 * heavy job, therefore a little memory caching could be handy. However, due to its huge size,
 * a proper value must be set.
 *
 * For your information, the [RsaKeyPair] instance consumes approximately 4KiB of memory per
 * instance with UTF-8 encoding.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
@AllOpen
@Component
internal class RsaKeyPairManagerImpl @Inject constructor(
        appProperties: AppProperties,
        private val keystoreRepo: RsaKeyStoreRepository
) : RsaKeyPairManager {
    @VisibleForTesting
    internal var lruCache = FastCollectedLruCache.create<String, JwtRsaKeyPair>(LRU_CACHE_CAPACITY)

    @VisibleForTesting
    internal var pemHandler = PemKeyHandler()

    private var latestJwtRsaKp: JwtRsaKeyPair? = null

    private val tokenLifetime = appProperties.accessTokenAliveSecs.toLong()

    override fun getLatest(): JwtRsaKeyPair {
        val now = utcNow()
        if (latestJwtRsaKp.isValidAt(now)) {
            return requireNotNull(latestJwtRsaKp)
        }

        synchronized(LATEST_ENTRY_WRITE_LOCK) {
            if (latestJwtRsaKp == null) {
                keystoreRepo.findLatestOneYoungerThan(now.minusSeconds(tokenLifetime))
                        .takeIf { it.isPresent }?.run { get() }?.let {
                            return cache(deriveJwtRsaKeyPair(it))
                        }

                val rawKeyPair = KeyPairGenerator.getInstance("RSA", "BC").run {
                    initialize(RSA_KEY_SIZE, SecureRandom())
                    return@run generateKeyPair()
                }

                val keyId = UUID.randomUUID().toString()
                val publicKey = rawKeyPair.public as RSAPublicKey
                val privateKey = rawKeyPair.private as RSAPrivateKey
                saveRsaKeyPair(keyId, publicKey, privateKey, now)

                cache(deriveJwtRsaKeyPair(keyId, publicKey, privateKey,
                        now.plusSeconds(tokenLifetime)))
            }

            return requireNotNull(latestJwtRsaKp)
        }
    }

    override fun getById(id: String): JwtRsaKeyPair {
        lruCache.get(id).takeIf { it != null }?.let { return it }

        keystoreRepo.findById(id).takeIf { it.isPresent }?.run { get() }?.let {
            with(deriveJwtRsaKeyPair(it)) {
                lruCache.put(keyId, this)
                return this
            }
        }

        throw IllegalArgumentException("Unknown JWT Key ID: $id")
    }

    override fun invalidate(id: String) {
        val rsaKeyPair = keystoreRepo.findById(id).takeIf { it.isPresent }?.run { get() }?.apply {
            isEnabled = false
        } ?: return

        keystoreRepo.save(rsaKeyPair)
    }

    private fun JwtRsaKeyPair?.isValidAt(now: LocalDateTime): Boolean {
        if (this == null) {
            return false
        }

        return now < expiredAt.minusMinutes(EXPIRY_TOLERANCE_CLOCK_SKEW_MINS)
    }

    private fun saveRsaKeyPair(keyId: String, pubKey: Key, privKey: Key, issuedAt: LocalDateTime) =
            keystoreRepo.save(RsaKeyPair().apply {
                id = keyId
                isEnabled = true
                publicKey = pemHandler.toPemFormat(pubKey)
                privateKey = pemHandler.toPemFormat(privKey)
                this.issuedAt = issuedAt
            })

    private fun deriveJwtRsaKeyPair(rsaKeyPair: RsaKeyPair): JwtRsaKeyPair =
            with(rsaKeyPair) {
                val rsaPubKey = pemHandler.fromPemFormat(publicKey) as RSAPublicKey
                val rsaPrivKey = pemHandler.fromPemFormat(privateKey) as RSAPrivateKey
                return@with deriveJwtRsaKeyPair(id, rsaPubKey, rsaPrivKey,
                        issuedAt.plusSeconds(tokenLifetime))
            }

    private fun deriveJwtRsaKeyPair(keyId: String, publicKey: RSAPublicKey,
                                    privateKey: RSAPrivateKey, expiredAt: LocalDateTime)
            : JwtRsaKeyPair {
        return JwtRsaKeyPair(keyId, RSASSAVerifier(publicKey), RSASSASigner(privateKey),
                expiredAt)
    }

    private fun cache(keyPair: JwtRsaKeyPair): JwtRsaKeyPair {
        lruCache.remove(keyPair.keyId)
        lruCache.put(keyPair.keyId, keyPair)
        latestJwtRsaKp = keyPair

        return requireNotNull(latestJwtRsaKp)
    }

    companion object {
        private val LATEST_ENTRY_WRITE_LOCK = Any()

        /** The cache will take approx. 300KiB(4KiB * 75 + SoftReference) of memory. */
        private const val LRU_CACHE_CAPACITY = 100
        private const val RSA_KEY_SIZE = 2048
        private const val EXPIRY_TOLERANCE_CLOCK_SKEW_MINS = 5L
    }
}
