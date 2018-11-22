/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth

import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.mvc.security.internal.HttpServletRequestAuthorizationHeaderFilter
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.persistence.entity.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import java.security.MessageDigest
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
interface AccessTokenBusiness {
    fun hash(data: ByteArray): ByteArray = MessageDigest.getInstance("SHA-1")
            .apply { update(data) }.run { digest() }

    fun findFromRequest(httpRequest: HttpServletRequest): String =
            HttpServletRequestAuthorizationHeaderFilter
                    .findAuthorizationHeader(httpRequest)?.token ?: ""

    fun create(user: User, timestamp: LocalDateTime = utcNow()): String

    @Throws(AuthTokenException::class)
    fun validate(token: String): Authentication

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(AccessTokenBusiness::class.java)
    }
}
