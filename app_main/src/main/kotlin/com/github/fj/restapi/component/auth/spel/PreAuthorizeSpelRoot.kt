/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.spel

import com.github.fj.restapi.component.auth.AuthenticationObjectImpl
import com.github.fj.restapi.persistence.consts.account.Role
import org.springframework.context.ApplicationContext
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.web.util.matcher.IpAddressMatcher
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder

/**
 * A reinvented wheel version of [org.springframework.security.access.expression.method.MethodSecurityExpressionRoot].
 *
 * This class is useless if a proper Spring security configuration will have been made.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Nov - 2018
 */
class PreAuthorizeSpelRoot(
        private val applicationContext: ApplicationContext,
        private val authentication: AuthenticationObjectImpl
) : PreAuthorizeExpression {
    /**
     * Implementation detail copied from [org.springframework.security.config.method.GlobalMethodSecurityBeanDefinitionParser.AbstractGrantedAuthorityDefaultsBeanFactory]
     */
    private val rolePrefix: String by lazy {
        applicationContext.getBeanNamesForType(GrantedAuthorityDefaults::class.java)
                .takeIf { it.size == 1 }?.let {
                    applicationContext.getBean(it[0], GrantedAuthorityDefaults::class.java)
                            .rolePrefix
                } ?: "ROLE_"
    }

    override fun hasRole(roleStr: String): Boolean = Role.of(rolePrefix + roleStr).let {
        if (it == Role.UNDEFINED) {
            throw UnsupportedOperationException("$roleStr is not appropriate for specifying any " +
                    "role. Use ${rolePrefix + roleStr} instead.")
        } else {
            return true
        }
    }

    override fun hasAnyRole(vararg roles: String): Boolean =
            roles.map { Role.of(it) }.any {
                it == authentication.details.role
            }

    override fun hasAuthority(authorityStr: String): Boolean =
            authentication.authorities.contains(Role.getAuthorityOf(authorityStr))

    override fun hasAnyAuthority(vararg authorities: String): Boolean {
        authorities.map { Role.getAuthorityOf(it) }.forEach {
            if (authentication.authorities.contains(it)) {
                return true
            }
        }

        return false
    }

    override fun hasIpAddress(ipAddressStr: String): Boolean {
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)
                ?.request
                ?: throw UnsupportedOperationException("Not called in the HTTP request context.")

        return IpAddressMatcher(ipAddressStr).matches(request)
    }

    // Yes to all: no challenges will be made
    override fun permitAll(): Boolean = true

    override fun isAnonymous(): Boolean = authentication.details.role.isAnonymous()

    // No to all: No sessions are used according to com.github.fj.restapi.appconfig.mvc.security.SecurityConfig
    override fun isRememberMe(): Boolean = false

    // No to all: no authorisation will be made
    override fun denyAll(): Boolean = false

    override fun isAuthenticated(): Boolean = authentication.isAuthenticated

    override fun isFullyAuthenticated(): Boolean = authentication.isAuthenticated
}
