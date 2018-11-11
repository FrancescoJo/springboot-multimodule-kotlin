/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.vo.account.AccessToken
import org.springframework.security.core.Authentication
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
interface AuthenticationBusiness {
    fun hash(data: ByteArray): ByteArray

    fun findAccessTokenFrom(httpRequest: HttpServletRequest): AccessToken?

    fun createAccessToken(user: User, timestamp: LocalDateTime = utcNow()): AccessToken

    /**
     * @param token Base62 encoded access token.
     * @throws UnknownAuthTokenException if access token is malformed or not issued.
     */
    @Throws(UnknownAuthTokenException::class)
    fun parseAccessToken(token: String): AccessToken

    @Throws(AuthTokenException::class)
    fun authenticate(token: AccessToken): Authentication
}
