/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.spel

/**
 * A reinvented wheel version of [org.springframework.security.access.expression.SecurityExpressionRoot].
 * All methods of [org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl]
 * are supported.
 *
 * This class is useless if a proper Spring security configuration will have been made.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Nov - 2018
 */
interface PreAuthorizeExpression {
    fun hasRole(roleStr: String): Boolean

    fun hasAnyRole(vararg roles: String): Boolean

    fun hasAuthority(authorityStr: String): Boolean

    fun hasAnyAuthority(vararg authorities: String): Boolean

    fun hasIpAddress(ipAddressStr: String): Boolean

    fun permitAll(): Boolean

    fun isAnonymous(): Boolean

    fun isRememberMe(): Boolean

    fun denyAll(): Boolean

    fun isAuthenticated(): Boolean

    fun isFullyAuthenticated(): Boolean
}
