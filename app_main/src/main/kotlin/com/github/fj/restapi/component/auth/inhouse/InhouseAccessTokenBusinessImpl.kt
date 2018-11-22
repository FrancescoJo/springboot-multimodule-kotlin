/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.inhouse

import com.github.fj.lib.collection.getSecureRandomBytes
import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcLocalDateTimeOf
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.auth.AuthenticationObjectImpl
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import io.seruco.encoding.base62.Base62
import org.springframework.security.core.Authentication
import java.nio.ByteBuffer
import java.security.Key
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
internal class InhouseAccessTokenBusinessImpl(
        private val appProperties: AppProperties,
        private val userRepository: UserRepository
) : AccessTokenBusiness {
    // TODO: Add force expire functionality (Utilise RSAKeyStore)
    //       Structure: KeyId(UUID 16) - Payload
    // Doing so, we can get:
    //     1. Key spinning. Cause a huge headache to attackers
    //     2. Force token expiration feature.
    var accessTokenMode = Encoding.RANDOM

    private val aes256CipherKey: Key
        get() = SecretKeySpec(appProperties.accessTokenAes256Key, CIPHER_ALG)

    private val accessTokenLifeSeconds: Long
        get() = appProperties.accessTokenAliveSecs.toLong()

    /**
     * This access token creation is implemented in a naïve way. It just focuses on "randomness"
     * of resulting Base62 represented data, to make attackers spend more time.
     * However a good witnessing attacker can point out that from 5th to 8th bytes seems having a
     * 'fixed rule' on multiple requests in a very short time.
     *
     * Moreover, this access token contains a crucial user information, such as id and login type,
     * that may could be a very effective attack vector.
     *
     * Just consider this as AES256 and Base62 encoding usage demonstration.
     */
    override fun create(user: User, timestamp: LocalDateTime): String {
        val mode = when (accessTokenMode) {
            Encoding.RANDOM -> {
                if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                    Encoding.FORWARD
                } else {
                    Encoding.BACKWARD
                }
            }
            else -> accessTokenMode
        }
        val ivArray = getSecureRandomBytes(LENGTH_IV_HEADER)

        createToken(user, mode, aes256CipherKey, ivArray, timestamp).let {
            return Base62.createInstance().encode(it.raw).toString(Charsets.UTF_8)
        }
    }

    @Throws(AuthTokenException::class)
    override fun validate(token: String): Authentication {
        val rawToken = Base62.createInstance().decode(token.toByteArray()).let {
            val iv = Arrays.copyOfRange(it, 0, LENGTH_IV_HEADER)
            val payload = Arrays.copyOfRange(it, LENGTH_IV_HEADER, it.size)

            return@let parseToken(aes256CipherKey, payload, iv)
        }

        val user = userRepository.findByIdToken(rawToken.uIdToken)
                .takeIf { u -> u.isPresent }?.get()
                ?: throw UnknownAuthTokenException("This token is tampered.")

        user.run {
            if (idToken != rawToken.uIdToken) {
                throw UnknownAuthTokenException("This token is tampered.")
            }

            if (Objects.hash(platformType.name, loginType.name) != rawToken.loginPlatformHash) {
                throw UnknownAuthTokenException("This token is tampered.")
            }

            if (createdDate.truncatedTo(ChronoUnit.SECONDS) != rawToken.registeredAt) {
                throw UnknownAuthTokenException("This token is tampered.")
            }
        }

        val now = utcNow().truncatedTo(ChronoUnit.SECONDS)
        if (rawToken.issuedAt > now) {
            throw UnknownAuthTokenException("This token is tampered.")
        }

        val tokenExpiration = rawToken.issuedAt.plusSeconds(accessTokenLifeSeconds)
        if (now > tokenExpiration) {
            throw AuthTokenExpiredException("This token is expired.")
        }

        return AuthenticationObjectImpl(user, token)
    }

    companion object {
        private const val CIPHER_ALG = "AES"
        private const val LENGTH_BYTES_HEADER = 16
        private const val LENGTH_PAYLOAD = 32
        private const val LENGTH_IV_HEADER = 16
        private const val INTEGER_BITS_LEN = 32
        private const val MASKER_BIT_POS_MOD = 30
        private const val BITMASK_LSB_0 = -2
        private const val BITMASK_LSB_1 = 1

        // Suppress: Necessary evil for adding randomness of our token.
        @Suppress("ComplexMethod", "ReturnCount")
        private fun createToken(user: User, encoding: Encoding, key: Key, iv: ByteArray,
                                now: LocalDateTime): DeserialisedToken {
            val garbageFiller: (ByteBuffer) -> Unit = { buf ->
                getSecureRandomBytes(buf.remaining()).let { buf.put(it, 0, it.size) }
            }

            val header = ByteBuffer.allocate(LENGTH_BYTES_HEADER).apply {
                val random = ThreadLocalRandom.current()
                /*
                 * We only need 1 bit to determine encoding. The directional information will be
                 * placed on position of (base % 31) and FORWARD for bit value 1, BACKWARD for
                 * bit value 0.
                 */
                val direction = random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE)
                val positionBase = random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
                        .toIntByBitPosition(random.nextInt(0, INTEGER_BITS_LEN))
                val position = 1 + Math.abs(positionBase % MASKER_BIT_POS_MOD)

                val head = (1 shl position).let {
                    val filtered = if (encoding == Encoding.FORWARD) {
                        direction and BITMASK_LSB_0
                    } else {
                        direction or BITMASK_LSB_1
                    }

                    return@let if (encoding == Encoding.FORWARD) {
                        filtered or it          // Sign bit: 1
                    } else {
                        filtered and it.inv()   // Sign bit: 0
                    }
                }
                putInt(head)
                putInt(positionBase)
                garbageFiller.invoke(this)
            }.array()

            val uIdToken = user.idToken
            val uIdTokenBytes = uIdToken.toByteArray()
            val loginPlatformHash = Objects.hash(user.platformType.name, user.loginType.name)
            // These will cause a problem after year 2038
            val issuedAt = now.utcEpochSecond().toInt()
            val registeredAt = user.createdDate.utcEpochSecond().toInt()

            val payload = ByteBuffer.allocate(LENGTH_PAYLOAD).apply {
                val uIdTokenLengthWriter: (ByteBuffer) -> Unit = { buf ->
                    buf.put(uIdTokenBytes.size.toByte())
                }
                val uIdTokenWriter: (ByteBuffer) -> Unit = { buf ->
                    uIdTokenBytes.forEach { buf.put(it) }
                }
                val loginTypeWriter: (ByteBuffer) -> Unit = { it.putInt(loginPlatformHash) }
                val timestampWriter: (ByteBuffer) -> Unit = { it.putInt(issuedAt) }
                val registeredDateWriter: (ByteBuffer) -> Unit = { it.putInt(registeredAt) }

                uIdTokenLengthWriter.invoke(this)
                listOf(uIdTokenWriter, loginTypeWriter, timestampWriter,
                        registeredDateWriter).let {
                    return@let if (encoding == Encoding.FORWARD) {
                        it
                    } else {
                        it.reversed()
                    }
                }.forEach {
                    it.invoke(this)
                }
                garbageFiller.invoke(this)
            }.array()

            val encrypted = ByteBuffer.allocate(header.size + LENGTH_IV_HEADER +
                    LENGTH_PAYLOAD).apply {
                put(header)

                val innerIv = getSecureRandomBytes(LENGTH_IV_HEADER)
                when (encoding) {
                    Encoding.FORWARD -> {
                        put(innerIv)
                        put(encrypt(key, innerIv, payload))
                    }
                    Encoding.BACKWARD -> {
                        put(encrypt(key, innerIv, payload).reversedArray())
                        put(innerIv.reversedArray())
                    }
                    else -> throw UnknownAuthTokenException("$encoding encoding strategy is " +
                            "unsupported.")
                }
            }.array().let { encrypt(key, iv, it) }

            val raw = ByteBuffer.allocate(LENGTH_IV_HEADER + encrypted.size).apply {
                put(iv)
                put(encrypted)
            }.array()

            return DeserialisedToken(raw, uIdToken, loginPlatformHash,
                    utcLocalDateTimeOf(issuedAt), utcLocalDateTimeOf(registeredAt))
        }

        // Suppress: Necessary evil for adding randomness of our token.
        @Suppress("ComplexMethod")
        private fun parseToken(aes256CipherKey: Key, rawToken: ByteArray, iv: ByteArray):
                DeserialisedToken {
            val decodedToken = decrypt(aes256CipherKey, iv, rawToken)
            val decodedBuf = ByteBuffer.wrap(decodedToken)

            val mode = ByteArray(LENGTH_BYTES_HEADER).let {
                decodedBuf.get(it)
                return@let it.parseEncoding()
            }

            val uIdToken: String
            val loginPlatformHash: Int
            val issuedAt: Int
            val registeredAt: Int

            val uIdTokenLengthReader: (ByteBuffer) -> Any = { it.get().toInt() }
            val uIdTokenReader: (Int, ByteBuffer) -> Any = { length, buf ->
                val uIdTokenArray = ByteArray(length)
                for (i in 0 until length) {
                    uIdTokenArray[i] = buf.get()
                }

                String(uIdTokenArray)
            }
            val loginTypeReader: (ByteBuffer) -> Any = { it.int }
            val timestampReader: (ByteBuffer) -> Any = { it.int }
            val registeredDateReader: (ByteBuffer) -> Any = { it.int }

            when (mode) {
                Encoding.FORWARD -> {
                    val innerIv = ByteArray(LENGTH_IV_HEADER).apply { decodedBuf.get(this) }
                    val payloadBuf = ByteBuffer.wrap(decrypt(aes256CipherKey, innerIv,
                            ByteArray(LENGTH_PAYLOAD).apply {
                                decodedBuf.get(this)
                            }
                    ))

                    val length = uIdTokenLengthReader(payloadBuf) as Int
                    uIdToken = uIdTokenReader(length, payloadBuf) as String
                    loginPlatformHash = loginTypeReader(payloadBuf) as Int
                    issuedAt = timestampReader(payloadBuf) as Int
                    registeredAt = registeredDateReader(payloadBuf) as Int
                }
                Encoding.BACKWARD -> {
                    val encodedPayload = ByteArray(LENGTH_PAYLOAD).apply {
                        decodedBuf.get(this)
                    }.reversedArray()
                    val innerIv = ByteArray(LENGTH_IV_HEADER).apply {
                        decodedBuf.get(this)
                    }.reversedArray()
                    val payloadBuf = ByteBuffer.wrap(
                            decrypt(aes256CipherKey, innerIv, encodedPayload)
                    )

                    val length = uIdTokenLengthReader(payloadBuf) as Int
                    registeredAt = registeredDateReader(payloadBuf) as Int
                    issuedAt = timestampReader(payloadBuf) as Int
                    loginPlatformHash = loginTypeReader(payloadBuf) as Int
                    uIdToken = uIdTokenReader(length, payloadBuf) as String
                }
                else -> throw UnknownAuthTokenException("$mode encoding strategy is unsupported.")
            }

            return DeserialisedToken(rawToken, uIdToken, loginPlatformHash,
                    utcLocalDateTimeOf(issuedAt), utcLocalDateTimeOf(registeredAt))
        }

        private fun ByteArray.parseEncoding(): Encoding {
            @Suppress("UsePropertyAccessSyntax")
            with(ByteBuffer.wrap(this)) {
                val head = getInt()
                val position = 1 shl (1 + Math.abs(getInt() % MASKER_BIT_POS_MOD))
                val parity = Math.abs(head % 2)

                return if (parity == 0) {
                    // Value of sign bit must be '1' to match 'FORWARD' mode
                    if ((head and position) == position) {
                        Encoding.FORWARD
                    } else {
                        throw UnknownAuthTokenException("Malformed access token.")
                    }
                } else {
                    // Value of sign bit must be '0' to match 'BACKWARD' mode
                    if ((head and position == 0)) {
                        Encoding.BACKWARD
                    } else {
                        throw UnknownAuthTokenException("Malformed access token.")
                    }
                }
            }
        }

        /**
         * @param start must be 0 to 31 or the result will be overflowed
         */
        private fun Long.toIntByBitPosition(start: Int): Int {
            val rightShifted = ushr(start)
            val mask = (1L shl INTEGER_BITS_LEN) - 1L
            return (rightShifted and mask).toInt()
        }

        /**
         * @param key the most important parameter to determine this block cipher operation,
         * length should be one of 128, 192 or 256 bits.
         * @param plainText length of it must be multiple of key length in order to use AES scheme,
         * since this logic assumes no padding block cipher mode.
         */
        private fun encrypt(key: Key, iv: ByteArray, plainText: ByteArray): ByteArray {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding").apply {
                init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
            }

            return cipher.doFinal(plainText)
        }

        /**
         * @param key the most important parameter to determine this block cipher operation,
         * length should be one of 128, 192 or 256 bits.
         * @param cipherText length of it must be multiple of key length in order to use AES scheme,
         * since this logic assumes no padding block cipher mode.
         */
        private fun decrypt(key: Key, iv: ByteArray, cipherText: ByteArray): ByteArray {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding").apply {
                init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
            }

            return cipher.doFinal(cipherText)
        }
    }
}
