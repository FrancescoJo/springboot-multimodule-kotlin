/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.security

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.persistence.entity.RsaKeyPair
import com.github.fj.restapi.persistence.repository.RsaKeyStoreRepository
import com.github.fj.restapi.util.FastCollectedLruCache
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import org.springframework.stereotype.Component
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
    internal var lruCache = FastCollectedLruCache.create<String, RsaKeyPairEntry>(100)

    private var latestEntry: RsaKeyPairEntry? = null

    private val tokenLifetime = appProperties.accessTokenAliveSecs.toLong()

    override fun getLatest(): RsaKeyPairEntry {
        val now = utcNow()
        if (latestEntry.isValidAt(now)) {
            return requireNotNull(latestEntry)
        }

        synchronized(LATEST_ENTRY_WRITE_LOCK) {
            if (latestEntry == null) {
                val maybeLatestRsaKeyPair =
                        keystoreRepo.findLatestOneYoungerThan(now.minusSeconds(tokenLifetime))
                if (maybeLatestRsaKeyPair.isPresent) {
                    val newEntry = deriveRsaKeyPairEntry(maybeLatestRsaKeyPair.get())
                    latestEntry = newEntry
                    return newEntry
                }

                val rawKeyPair = KeyPairGenerator.getInstance("RSA", "BC").run {
                    initialize(RSA_KEY_SIZE, SecureRandom())
                    return@run generateKeyPair()
                }

                val newEntry = deriveRsaKeyPairEntry(UUID.randomUUID().toString(),
                        rawKeyPair.public as RSAPublicKey, rawKeyPair.private as RSAPrivateKey,
                        now.plusSeconds(tokenLifetime))
                latestEntry = newEntry
                // TODO: -create-, -reference-, <persist>

                // TODO: KeyPair to PEM format
            }

            return requireNotNull(latestEntry)
        }
    }

    override fun getById(id: Long): RsaKeyPairEntry {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun RsaKeyPairEntry?.isValidAt(now: LocalDateTime): Boolean {
        if (this == null) {
            return false
        }

        return now < expiredAt.minusMinutes(EXPIRY_TOLERANCE_CLOCK_SKEW_MINS)
    }

    private fun deriveRsaKeyPairEntry(rsaKeyPair: RsaKeyPair): RsaKeyPairEntry = with(rsaKeyPair) {
        // TODO: read PEM format as Public/Private RSA Keys
        return@with deriveRsaKeyPairEntry(id, publicKey as RSAPublicKey,
                privateKey as RSAPrivateKey, issuedAt.plusSeconds(tokenLifetime))
    }

    private fun deriveRsaKeyPairEntry(keyId: String, publicKey: RSAPublicKey,
                                      privateKey: RSAPrivateKey, expiredAt: LocalDateTime)
            : RsaKeyPairEntry {
        return RsaKeyPairEntry(keyId, RSASSAVerifier(publicKey), RSASSASigner(privateKey),
                expiredAt)
    }

    companion object {
        private val LATEST_ENTRY_WRITE_LOCK = Any()

        private const val RSA_KEY_SIZE = 2048
        private const val EXPIRY_TOLERANCE_CLOCK_SKEW_MINS = 5L
    }
}
