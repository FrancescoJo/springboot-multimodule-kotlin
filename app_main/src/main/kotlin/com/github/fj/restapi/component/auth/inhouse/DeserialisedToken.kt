/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.inhouse

import java.time.LocalDateTime

/**
 * An intermediate object which
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
internal data class DeserialisedToken(
        val raw: ByteArray,

        val uIdToken: String,

        val loginPlatformHash: Int,

        val issuedAt: LocalDateTime,

        val registeredAt: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (other !is DeserialisedToken) {
            return false
        }

        return raw.contentEquals(other.raw) &&
                uIdToken.contentEquals(other.uIdToken) &&
                loginPlatformHash == other.loginPlatformHash &&
                issuedAt == other.issuedAt &&
                registeredAt == other.registeredAt
    }

    override fun hashCode(): Int {
        var result = raw.contentHashCode()
        result = 31 * result + uIdToken.hashCode()
        result = 31 * result + loginPlatformHash
        result = 31 * result + issuedAt.hashCode()
        result = 31 * result + registeredAt.hashCode()
        return result
    }
}
