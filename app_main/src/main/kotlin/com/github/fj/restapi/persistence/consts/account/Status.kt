/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.github.fj.lib.annotation.UndefinableEnum

/**
 * Stores user information which is likely to be changed.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@UndefinableEnum
enum class Status(val key: String) {
    NORMAL("n"),
    WITHDRAWN("_"),
    SUSPENDED("s"),
    BANNED("b"),
    UNDEFINED("");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun byKey(key: String?) = Status.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
