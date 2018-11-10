/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.mvc.security.internal.HttpServletRequestAuthorizationHeaderFilter
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.appconfig.mvc.security.internal.AuthenticationObjectImpl
import com.github.fj.restapi.exception.account.UnauthorisedException
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.vo.account.AccessToken
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@AllOpen
@Configuration
class ControllerParamsConfig @Inject constructor(
        private val authBusiness: AuthenticationBusiness
) : WebMvcConfigurer {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserArgumentResolver())
        argumentResolvers.add(AccessTokenArgumentResolver(authBusiness))
    }

    private class UserArgumentResolver : HandlerMethodArgumentResolver {
        override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                     webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
            val currentSecurityContext = (SecurityContextHolder.getContext()?.authentication
                    ?: throw UnauthorisedException("You are not authorised.")) as? AuthenticationObjectImpl
                    ?: throw UnsupportedOperationException("Your authentication kind is not supported.")

            return currentSecurityContext.details
        }

        override fun supportsParameter(parameter: MethodParameter): Boolean =
                parameter.parameterType == User::class.java
    }

    private class AccessTokenArgumentResolver(private val authBusiness: AuthenticationBusiness)
        : HandlerMethodArgumentResolver {
        override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                     webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
            val currentSecurityContext = SecurityContextHolder.getContext()?.authentication
                    ?: return null

            if (currentSecurityContext is AuthenticationObjectImpl) {
                return currentSecurityContext.principal
            }

            val httpServletRequest = webRequest.nativeRequest as? HttpServletRequest
                    ?: webRequest as? HttpServletRequest
                    ?: throw UnknownAuthTokenException("Unable to detect current security context.")

            val token = HttpServletRequestAuthorizationHeaderFilter.findAuthorizationHeader(httpServletRequest)
                    ?: throw UnknownAuthTokenException("No HTTP Authorization header has been found.")

            return authBusiness.parseAccessToken(token.token)
        }

        override fun supportsParameter(parameter: MethodParameter): Boolean =
                parameter.parameterType == AccessToken::class.java
    }
}
