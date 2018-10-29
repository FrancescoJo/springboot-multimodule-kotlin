/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.github.fj.lib.annotation.UndefinableEnum

/**
 * Represents one of client's platform type.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@UndefinableEnum
enum class PlatformType(val key: String) {
    /** Basic authentication: user provided name and password */
    ANDROID("a"),
    IOS("i"),
    WEB("w"),
    UNDEFINED("");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun byKey(key: String?) = PlatformType.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
