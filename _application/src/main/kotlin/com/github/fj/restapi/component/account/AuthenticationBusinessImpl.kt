/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.dto.account.AccessToken
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.MyAuthentication
import com.github.fj.restapi.persistence.entity.User
import io.seruco.encoding.base62.Base62
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.security.Key
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
@AllOpen
@Component
class AuthenticationBusinessImpl(private val appProperties: AppProperties) : AuthenticationBusiness {
    var accessTokenMode = AccessToken.Encoded.RANDOM

    private val aes256CipherKey: Key
        get() = SecretKeySpec(appProperties.accessTokenAes256Key, CIPHER_ALG)

    @Suppress("UnstableApiUsage")
    override fun hash(data: ByteArray): ByteArray =
            com.google.common.hash.Hashing.goodFastHash(data.size * 8).hashBytes(data).asBytes()

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
    override fun createAccessToken(user: User): MyAuthentication {
        val now = utcNow()
        val mode = when (accessTokenMode) {
            AccessToken.Encoded.RANDOM -> with(AccessToken.Encoded.values()) {
                get(Random().nextInt(size - 1))
            }
            else -> accessTokenMode
        }
        val ivArray = ByteArray(LENGTH_IV_HEADER).apply { SecureRandom().nextBytes(this) }

        return MyAuthentication().apply {
            id = user.id
            encoding = mode
            iv = ivArray
            accessToken = createAccessToken(user, mode, aes256CipherKey, ivArray, now)
            rawAccessToken = requireNotNull(accessToken).raw.toByteArray()
            issuedDate = now
        }
    }

    override fun parseAccessToken(token: String, iv: ByteArray): AccessToken {
        val rawToken = Base62.createInstance().decode(token.toByteArray())
        val decodedToken = decrypt(aes256CipherKey, iv, rawToken)
        val decodedBuf = ByteBuffer.wrap(decodedToken)

        val mode = ByteArray(LENGTH_BYTES_HEADER).let {
            decodedBuf.get(it)
            return@let it.parseEncoded()
        }

        val userId: Long
        val uIdTokenHash: Long
        val loginPlatformHash: Int
        val issuedTimestamp: Int
        val userRegisteredTimestamp: Int

        val userIdReader: (ByteBuffer) -> Any = { it.long * -1 }
        val userTagReader: (ByteBuffer) -> Any = { it.long }
        val loginTypeReader: (ByteBuffer) -> Any = { it.int }
        val timestampReader: (ByteBuffer) -> Any = { it.int }
        val registeredDateReader: (ByteBuffer) -> Any = { it.int }

        when (mode) {
            AccessToken.Encoded.FORWARD -> {
                val innerIv = ByteArray(LENGTH_IV_HEADER).apply { decodedBuf.get(this) }
                val payloadBuf = ByteBuffer.wrap(decrypt(aes256CipherKey, innerIv, ByteArray(LENGTH_PAYLOAD_HEADER)
                        .apply { decodedBuf.get(this) }))

                userId = userIdReader(payloadBuf) as Long
                uIdTokenHash = userTagReader(payloadBuf) as Long
                loginPlatformHash = loginTypeReader(payloadBuf) as Int
                issuedTimestamp = timestampReader(payloadBuf) as Int
                userRegisteredTimestamp = registeredDateReader(payloadBuf) as Int
            }
            AccessToken.Encoded.BACKWARD -> {
                val encodedPayload = ByteArray(LENGTH_PAYLOAD_HEADER).apply { decodedBuf.get(this) }.reversedArray()
                val innerIv = ByteArray(LENGTH_IV_HEADER).apply { decodedBuf.get(this) }.reversedArray()
                val payloadBuf = ByteBuffer.wrap(decrypt(aes256CipherKey, innerIv, encodedPayload))

                userRegisteredTimestamp = registeredDateReader(payloadBuf) as Int
                issuedTimestamp = timestampReader(payloadBuf) as Int
                loginPlatformHash = loginTypeReader(payloadBuf) as Int
                uIdTokenHash = userTagReader(payloadBuf) as Long
                userId = userIdReader(payloadBuf) as Long
            }
            else -> { throw UnknownAuthTokenException("$mode encoding strategy is unsupported.") }
        }

        return AccessToken(rawToken.toList(), mode, iv.toList(),
                userId, uIdTokenHash, loginPlatformHash, issuedTimestamp, userRegisteredTimestamp)
    }

    companion object {
        private const val CIPHER_ALG = "AES"
        private const val LENGTH_BYTES_HEADER = 16
        private const val LENGTH_PAYLOAD_HEADER = 32
        private const val LENGTH_IV_HEADER = 16

        private fun createAccessToken(user: User, mode: AccessToken.Encoded,
                                      key: Key, iv: ByteArray, now: LocalDateTime): AccessToken {
            val garbageFiller: (ByteBuffer) -> Unit = { buf ->
                ByteArray(buf.remaining()) { 0 }.apply {
                    SecureRandom().nextBytes(this)
                }.let {
                    buf.put(it, 0, it.size)
                }
            }

            val header = ByteBuffer.allocate(LENGTH_BYTES_HEADER).apply {
                val random = ThreadLocalRandom.current()
                /*
                 * We only need 1 bit to determine modes. The directional information will be placed on
                 * position of (base % 31) and FORWARD for bit value 1, BACKWARD for bit value 0.
                 */
                val direction = random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE)
                val positionBase = random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
                        .toIntByBitPosition(random.nextInt(0, 32))
                val position = 1 + Math.abs(positionBase % 30)

                val head = (1 shl position).let {
                    val filtered = if (mode == AccessToken.Encoded.FORWARD) {
                        direction and -2    // Bitmask: LSB 0
                    } else {
                        direction or 1      // Bitmask: LSB 1
                    }

                    return@let if (mode == AccessToken.Encoded.FORWARD) {
                        filtered or it          // Sign bit: 1
                    } else {
                        filtered and it.inv()   // Sign bit: 0
                    }
                }
                putInt(head)
                putInt(positionBase)
                garbageFiller.invoke(this)
            }.array()

            val userId = user.id
            val uIdTokenHash = with(user.idToken) {
                @Suppress("UnstableApiUsage")
                return@with com.google.common.hash.Hashing.goodFastHash(length * 8)
                        .hashString(this, kotlin.text.Charsets.UTF_8)
                        .asLong()
            }
            val loginPlatformHash = Objects.hash(user.platformType.name, user.loginType.name)
            // These will cause a problem after year 2038
            val timestamp = now.utcEpochSecond().toInt()
            val createdTimestamp = user.createdDate.utcEpochSecond().toInt()

            val payload = ByteBuffer.allocate(LENGTH_PAYLOAD_HEADER).apply {
                val userIdWriter: (ByteBuffer) -> Unit = { it.putLong(userId * -1) }
                val userTagWriter: (ByteBuffer) -> Unit = { it.putLong(uIdTokenHash) }
                val loginTypeWriter: (ByteBuffer) -> Unit = { it.putInt(loginPlatformHash) }
                val timestampWriter: (ByteBuffer) -> Unit = { it.putInt(timestamp) }
                val registeredDateWriter: (ByteBuffer) -> Unit = { it.putInt(createdTimestamp) }

                listOf(userIdWriter, userTagWriter, loginTypeWriter, timestampWriter,
                        registeredDateWriter).let {
                    if (mode == AccessToken.Encoded.FORWARD) {
                        return@let it
                    } else {
                        return@let it.reversed()
                    }
                }.forEach {
                    it.invoke(this)
                }
                garbageFiller.invoke(this)
            }.array()

            val encrypted = ByteBuffer.allocate(LENGTH_BYTES_HEADER + LENGTH_PAYLOAD_HEADER +
                    LENGTH_IV_HEADER).apply {
                put(header)

                val innerIv = ByteArray(LENGTH_IV_HEADER).apply { SecureRandom().nextBytes(this) }
                @Suppress("NON_EXHAUSTIVE_WHEN")
                when (mode) {
                    AccessToken.Encoded.FORWARD -> {
                        put(innerIv)
                        put(encrypt(key, innerIv, payload))
                    }
                    AccessToken.Encoded.BACKWARD -> {
                        put(encrypt(key, innerIv, payload).reversedArray())
                        put(innerIv.reversedArray())
                    }
                }
            }.array().let {
                return@let encrypt(key, iv, it)
            }

            return AccessToken(encrypted.toList(), mode, iv.toList(),
                    userId, uIdTokenHash, loginPlatformHash, timestamp, createdTimestamp)
        }

        private fun ByteArray.parseEncoded(): AccessToken.Encoded {
            @Suppress("UsePropertyAccessSyntax")
            return with(ByteBuffer.wrap(this)) {
                val head = getInt()
                val position = 1 shl (1 + Math.abs(getInt() % 30))

                return@with Math.abs(head % 2).let {
                    return@let if (it == 0) {
                        // Value of sign bit must be '1' to match 'FORWARD' mode
                        if ((head and position) == position) {
                            AccessToken.Encoded.FORWARD
                        } else {
                            throw UnknownAuthTokenException("Malformed access token.")
                        }

                    } else {
                        // Value of sign bit must be '0' to match 'BACKWARD' mode
                        if ((head and position == 0)) {
                            AccessToken.Encoded.BACKWARD
                        } else {
                            throw UnknownAuthTokenException("Malformed access token.")
                        }
                    }
                }
            }
        }

        /**
         * @param start must be 0 to 31 or the result will be overflowed
         */
        private fun Long.toIntByBitPosition(start: Int): Int {
            val rightShifted = ushr(start)
            val mask = (1L shl 32) - 1L
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
