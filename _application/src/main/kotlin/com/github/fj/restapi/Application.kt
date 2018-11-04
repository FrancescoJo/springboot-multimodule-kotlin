/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import com.google.common.base.Joiner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Spring boot startup class
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication(
        exclude = [UserDetailsServiceAutoConfiguration::class]
)
open class Application {
    companion object {
        private val configurationNames = arrayOf("application", "application-" + BuildConfig.currentProfile.value)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(Application::class.java)
                    .properties("spring.config.name:" +
                            Joiner.on(",").skipNulls().join(configurationNames))
                    .build()
                    .run(*args)
        }
    }
}
