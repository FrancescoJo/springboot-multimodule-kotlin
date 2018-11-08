/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security

import com.github.fj.lib.annotation.UndefinableEnum

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@UndefinableEnum
enum class HttpAuthScheme(val typeValue: String) {
    BASIC("Basic"),
    BEARER("Bearer"),
    TOKEN("Token"),
    UNDEFINED("");

    companion object {
        fun byTypeValue(value: String) = HttpAuthScheme.values().firstOrNull {
            it.typeValue == value
        } ?: UNDEFINED
    }
}
