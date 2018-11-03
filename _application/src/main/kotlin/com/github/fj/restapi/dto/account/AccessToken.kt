/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.account

import com.github.fj.lib.annotation.UndefinableEnum
import java.time.LocalDateTime

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

        val issuedTimestamp: LocalDateTime,

        val userRegisteredTimestamp: LocalDateTime
) {
    companion object {
        val EMPTY = AccessToken(
                emptyList(), Encoded.UNDEFINED, emptyList(), 0L, 0L, 0,
                LocalDateTime.MIN, LocalDateTime.MIN)
    }

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
