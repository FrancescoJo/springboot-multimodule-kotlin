/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.servlet

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.helper.RequestLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@AllOpen
@Configuration
class RequestInterceptorsConfig(private val requestLoggingInterceptor: RequestLoggingInterceptor) :
        WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**")
    }
}
