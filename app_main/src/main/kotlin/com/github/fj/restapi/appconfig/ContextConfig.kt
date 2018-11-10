/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.lib.annotation.AllOpen
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
@AllOpen
@Configuration
class ContextConfig @Inject constructor(
        private val defaultObjMapper: ObjectMapper
) : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        defaultObjMapper.registerModule(JacksonDeserialisationModule())
    }
}
