/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts

import com.github.fj.lib.annotation.UndefinableEnum

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@UndefinableEnum
enum class UserActivity(val key: String) {
    CREATE_ACCOUNT("+CA"),
    LOG_IN("+L"),
    GET_PROFILE("GP"),
    DELETE_ACCOUNT("-DA"),
    UNDEFINED("");

    companion object {
        fun byKey(key: String?) = UserActivity.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
