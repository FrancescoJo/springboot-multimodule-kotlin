/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.aop

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.aop.internal.EndpointAccessLogAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@AllOpen
@Configuration
@EnableAspectJAutoProxy
class AopConfig {
    @Bean
    fun endpointAccessLogAspect() = EndpointAccessLogAspect()
}
