/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import com.google.common.base.Joiner
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.security.Security

/**
 * Spring boot startup class
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication
open class Application {
    companion object {
        private val configurationNames = arrayOf("application", "application-" +
                BuildConfig.currentProfile.value)

        @JvmStatic
        fun main(args: Array<String>) {
            Security.addProvider(BouncyCastleProvider())

            @Suppress("SpreadOperator") // This logic is called only once.
            SpringApplicationBuilder(Application::class.java)
                    .properties("spring.config.name:" +
                            Joiner.on(",").skipNulls().join(configurationNames))
                    .build()
                    .run(*args)
        }
    }
}
