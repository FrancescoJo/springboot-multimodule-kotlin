/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo

import com.google.common.base.Joiner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    Application().main(args)
}

/**
 * Spring boot bootstrap class for boot-gs-multi-module
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication
open class Application {
    private val KEY_RUNNING_PROFILE = "EXAMPLE_APP_PROFILE"

    fun main(args: Array<String>) {
        val envs = System.getenv()
        val profValue = envs[KEY_RUNNING_PROFILE] ?: run {
            System.err.println("No system environment $KEY_RUNNING_PROFILE is set!")
            System.exit(1)
            ""
        }

        val appProfile = Profile.byValue(profValue)
        if (appProfile == null) {
            System.err.println("Illegal app profile is provided: $profValue. Must be one of following:")
            val profiles = Profile.values()
            for (i in 0 until profiles.size - 1) {
                System.err.print(profiles[i].value + ", ")
            }
            System.err.println(profiles[profiles.size - 1].value)
            System.exit(1)
        }

        val configurationNames = arrayOf("application", "application-" + appProfile!!.value)
        SpringApplicationBuilder(Application::class.java)
                .properties("spring.config.name:" + Joiner.on(",").skipNulls().join(configurationNames))
                .build()
                .run(*args)
    }

    enum class Profile constructor(internal var value: String) {
        DEV("dev"),
        ALPHA("alpha"),
        BETA("beta"),
        RELEASE("release");

        companion object {
            fun byValue(value: String) = Profile.values().firstOrNull { it.value.equals(value, ignoreCase = true) }
        }
    }
}
