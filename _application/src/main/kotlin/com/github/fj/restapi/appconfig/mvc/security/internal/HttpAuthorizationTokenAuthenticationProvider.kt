/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.restapi.appconfig.mvc.security.HttpAuthScheme
import com.github.fj.restapi.component.account.AuthenticationBusiness
import org.slf4j.Logger
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class HttpAuthorizationTokenAuthenticationProvider(
        private val log: Logger,
        private val authBusiness: AuthenticationBusiness
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication? {
        if (authentication == null) {
            log.warn("No authentication was provided. Mistake in Spring security configuration?")
            return null
        }

        val authToken = authentication as HttpAuthorizationToken
        val accessToken = when (authToken.scheme) {
            HttpAuthScheme.TOKEN -> authBusiness.parseAccessToken(authToken.token)
            else -> throw UnsupportedOperationException("$authToken type of authentication is not supported.")
        }

        return authBusiness.authenticate(accessToken)
    }

    override fun supports(authentication: Class<*>?): Boolean =
            HttpAuthorizationToken::class.java.isAssignableFrom(authentication)
}
