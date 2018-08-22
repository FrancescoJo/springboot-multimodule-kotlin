/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import com.google.common.base.Joiner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

/**
 * Spring boot startup class
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication
open class Application {
    protected lateinit var applicationContext: ConfigurableApplicationContext

    fun start(args: Array<String>) {
        val configurationNames = arrayOf("application", "application-" + BuildConfig.currentProfile)

        val context = SpringApplicationBuilder(Application::class.java)
                .properties("spring.config.name:" +
                            Joiner.on(",").skipNulls().join(configurationNames))
                .build()
                .run(*args)

        this.applicationContext = context
    }

    /**
     * This method is designed for integration tests
     */
    fun stop() {
        applicationContext.close()
    }

    companion object {
        val instance: Application
            get() = _instance ?: throw IllegalStateException("Application is not instantiated")

        // Private only backing field name
        @Suppress("ObjectPropertyName")
        private var _instance: Application? = null

        @JvmStatic
        fun main(args: Array<String>) {
            _instance = Application().apply { start(args) }
        }
    }
}
