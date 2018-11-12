/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@AllOpen
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestInterceptorsConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(RequestLoggingInterceptor())
                .addPathPatterns("${ApiPaths.API}/**")
    }
}
