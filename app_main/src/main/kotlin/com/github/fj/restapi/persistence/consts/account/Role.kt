/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.github.fj.lib.annotation.UndefinableEnum
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_ADMIN
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_ANONYMOUS
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_BY_ROLE
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_MODERATOR
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_PREMIUM_USER
import com.github.fj.restapi.persistence.consts.account.Authority.Companion.AUTHORITY_USER
import org.springframework.security.core.GrantedAuthority
import java.util.*

/**
 * Represents user's current role.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
@Suppress("MagicNumber")    // Numbers here are self documenting.
@UndefinableEnum
enum class Role(val key: Int, val roleName: String, val authorities: Collection<GrantedAuthority>) {
    ANONYMOUS(1, "Anonymous user", Collections.singleton(AUTHORITY_ANONYMOUS)),
    USER(10, "Normal user", Collections.singleton(AUTHORITY_USER)),
    PREMIUM_USER(20, "Premium user", Collections.unmodifiableCollection(
            setOf(AUTHORITY_USER, AUTHORITY_PREMIUM_USER))
    ),
    MODERATOR(30, "Moderator", Collections.unmodifiableCollection(
            setOf(AUTHORITY_USER, AUTHORITY_MODERATOR))
    ),
    ADMINISTRATOR(100, "Administrator", Collections.unmodifiableCollection(
            setOf(AUTHORITY_USER, AUTHORITY_MODERATOR, AUTHORITY_ADMIN))
    ),
    UNDEFINED(-1, "", Collections.emptyList());

    fun isAnonymous(): Boolean = this == ANONYMOUS

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return roleName
    }

    companion object {
        fun of(role: String): Role = try {
            Role.valueOf(role)
        } catch (e: IllegalArgumentException) {
            UNDEFINED
        }

        fun getAuthorityOf(authority: String): GrantedAuthority =
                AUTHORITY_BY_ROLE[authority] ?: AUTHORITY_ANONYMOUS

        fun byKey(key: Int?) = Role.values().firstOrNull { it.key == key } ?: UNDEFINED

        @JsonCreator
        @JvmStatic
        fun byRoleName(name: String?) = Role.values().firstOrNull { it.roleName == name }
                ?: UNDEFINED
    }
}
