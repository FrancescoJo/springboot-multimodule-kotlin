/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.restapi.component.auth.HttpAuthorizationToken
import com.github.fj.restapi.component.auth.HttpAuthScheme
import com.github.fj.restapi.component.auth.AccessTokenBusinessFactory
import com.github.fj.restapi.exception.AuthTokenException
import org.slf4j.Logger
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class HttpAuthorizationTokenAuthenticationProvider(
        private val accessTokenBizFactory: AccessTokenBusinessFactory,
        private val log: Logger
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication? {
        if (authentication == null) {
            log.warn("No authentication was provided. Mistake in Spring security configuration?")
            return null
        }

        return with(authentication as HttpAuthorizationToken) {
            when (scheme) {
                HttpAuthScheme.TOKEN -> getAuthentication(this)
                else -> throw UnsupportedOperationException(
                        "$this type of authentication is not supported.")
            }
        }
    }

    private fun getAuthentication(authentication: HttpAuthorizationToken): Authentication? {
        val authenticator = accessTokenBizFactory.get()
        val accessToken = authenticator.parse(authentication.token)
        val ourAuthentication = try {
            authenticator.validate(accessToken)
        } catch (e: AuthTokenException) {
            throw AuthenticationCredentialsNotFoundException(e.message, e)
        }

        log.trace("Authentication object was found: {}", ourAuthentication::class.qualifiedName)
        // We don't need to set this object into SecurityContextHolder, because Spring will do it for us.
        return ourAuthentication
    }

    override fun supports(authentication: Class<*>?): Boolean =
            HttpAuthorizationToken::class.java.isAssignableFrom(authentication)
}
