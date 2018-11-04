/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import org.slf4j.Logger
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class HttpAuthorizationTokenAuthenticationProvider(private val log: Logger) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication? {
        if (authentication == null) {
            log.warn("No authentication was provided. Mistake in Spring security configuration?")
            return null
        }

        val authToken = authentication as HttpAuthorizationToken

        TODO("NOT IMPLEMENTED- AUTHENTICATE $authToken")
    }

    override fun supports(authentication: Class<*>?): Boolean =
            HttpAuthorizationToken::class.java.isAssignableFrom(authentication)

    companion object {
        private fun Logger.t(message: String, vararg arguments: Any) {
            trace(String.format("[AuthProvider] %s", message), arguments)
        }
    }
}
