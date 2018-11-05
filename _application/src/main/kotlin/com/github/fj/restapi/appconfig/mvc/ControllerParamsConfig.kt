/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.component.account.AuthenticationObjectImpl
import com.github.fj.restapi.exception.account.UnauthorisedException
import com.github.fj.restapi.persistence.entity.User
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@AllOpen
@Configuration
class ControllerParamsConfig : WebMvcConfigurer {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserArgumentResolver())
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
}
