/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.github.fj.lib.annotation.UndefinableEnum
import com.github.fj.restapi.persistence.consts.account.RoleAuthority.Companion.ROLE_ADMIN
import com.github.fj.restapi.persistence.consts.account.RoleAuthority.Companion.ROLE_ANONYMOUS
import com.github.fj.restapi.persistence.consts.account.RoleAuthority.Companion.ROLE_MODERATOR
import com.github.fj.restapi.persistence.consts.account.RoleAuthority.Companion.ROLE_PREMIUM_USER
import com.github.fj.restapi.persistence.consts.account.RoleAuthority.Companion.ROLE_USER
import org.springframework.security.core.GrantedAuthority
import java.util.*

/**
 * Represents user's current role.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
@UndefinableEnum
enum class Role(val key: Int, val authorities: Collection<GrantedAuthority>) {
    ANONYMOUS(1, Collections.singleton(ROLE_ANONYMOUS)),
    USER(10, Collections.singleton(ROLE_USER)),
    PREMIUM_USER(20, Collections.unmodifiableCollection(setOf(ROLE_USER, ROLE_PREMIUM_USER))),
    MODERATOR(30, Collections.unmodifiableCollection(setOf(ROLE_USER, ROLE_MODERATOR))),
    ADMINISTRATOR(100, Collections.unmodifiableCollection(setOf(ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN))),
    UNDEFINED(-1, Collections.emptyList());

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): Int {
        return key
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun byKey(key: Int?) = Role.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
