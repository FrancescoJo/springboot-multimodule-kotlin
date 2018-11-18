/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
enum class TokenGenerationMethod(private val method: String) {
    JWT("jwt"),
    IN_HOUSE("inhouse"),
    DEFAULT("default");

    companion object {
        fun byMethod(value: String) = TokenGenerationMethod.values().firstOrNull {
            it.method.equals(value, true)
        } ?: DEFAULT
    }
}
