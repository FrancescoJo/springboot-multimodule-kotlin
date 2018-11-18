/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth

import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.User
import org.springframework.security.core.Authentication
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
interface AccessTokenBusiness {
    @Suppress("UnstableApiUsage")
    fun hash(data: ByteArray): ByteArray =
            com.google.common.hash.Hashing.goodFastHash(data.size * BITS_PER_BYTE)
                    .hashBytes(data).asBytes()

    fun findFromRequest(httpRequest: HttpServletRequest): String

    fun create(user: User, timestamp: LocalDateTime = utcNow()): String

    @Throws(AuthTokenException::class)
    fun validate(token: String): Authentication

    companion object {
        private const val BITS_PER_BYTE = 8
    }
}
