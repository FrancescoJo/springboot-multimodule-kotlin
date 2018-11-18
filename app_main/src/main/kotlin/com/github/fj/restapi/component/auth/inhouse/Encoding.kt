/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.inhouse

import com.github.fj.lib.annotation.UndefinableEnum

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
@UndefinableEnum
enum class Encoding(val key: String) {
    FORWARD("f"),
    BACKWARD("b"),
    RANDOM("r"),
    UNDEFINED("");

    companion object {
        @JvmStatic
        fun byKey(key: String?) = Encoding.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
