/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
enum class AppProfile constructor(internal var value: String) {
    DEV("dev"),
    ALPHA("alpha"),
    BETA("beta"),
    RELEASE("release");

    companion object {
        fun byValue(value: String) = AppProfile.values().firstOrNull {
            it.value.equals(value, ignoreCase = true)
        }
    }
}
