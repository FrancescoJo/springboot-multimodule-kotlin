/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.account

import com.github.fj.lib.annotation.UndefinableEnum
import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.User
import io.seruco.encoding.base62.Base62
import java.nio.ByteBuffer
import java.security.Key
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
data class AccessToken(
        val raw: List<Byte>,

        val mode: Encoded,

        val iv: List<Byte>,

        val userId: Long,

        val uIdTokenHash: Long,

        val loginPlatformHash: Int,

        val issuedTimestamp: Int,

        val userRegisteredTimestamp: Int
) {
    @UndefinableEnum
    enum class Encoded(val key: String) {
        FORWARD("f"),
        BACKWARD("b"),
        RANDOM("r"),
        UNDEFINED("");

        companion object {
            @JvmStatic
            fun byKey(key: String?) = Encoded.values().firstOrNull { it.key == key } ?: UNDEFINED
        }
    }
}
