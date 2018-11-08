/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.restapi.appconfig.mvc.security.HttpAuthScheme
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
data class HttpAuthorizationToken(val scheme: HttpAuthScheme, val token: String) :
        AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any = scheme

    override fun getPrincipal(): Any = token
}
