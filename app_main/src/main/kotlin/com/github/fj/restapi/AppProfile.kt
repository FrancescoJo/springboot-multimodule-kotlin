/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
enum class AppProfile constructor(internal val value: String, internal val isDeveloperMode: Boolean) {
    DEV("dev", true),
    ALPHA("alpha", true),
    BETA("beta", false),
    RELEASE("release", false);

    companion object {
        fun byValue(value: String) = AppProfile.values().firstOrNull {
            it.value.equals(value, ignoreCase = true)
        }
    }
}
