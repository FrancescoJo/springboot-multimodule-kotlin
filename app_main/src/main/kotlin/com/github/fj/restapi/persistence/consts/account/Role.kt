/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.github.fj.lib.annotation.UndefinableEnum
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

private val AUTHORITY_ANONYMOUS: GrantedAuthority = SimpleGrantedAuthority("ANONYMOUS")
private val AUTHORITY_USER: GrantedAuthority = SimpleGrantedAuthority("USER")
private val AUTHORITY_PREMIUM_USER: GrantedAuthority = SimpleGrantedAuthority("PREMIUM_USER")
private val AUTHORITY_MODERATOR: GrantedAuthority = SimpleGrantedAuthority("MODERATOR")
private val AUTHORITY_ADMIN: GrantedAuthority = SimpleGrantedAuthority("ADMIN")
private val AUTHORITY_BY_ROLE = HashMap<String, GrantedAuthority>().apply {
    put("ANONYMOUS", AUTHORITY_ANONYMOUS)
    put("USER", AUTHORITY_USER)
    put("PREMIUM_USER", AUTHORITY_PREMIUM_USER)
    put("MODERATOR", AUTHORITY_MODERATOR)
    put("ADMIN", AUTHORITY_ADMIN)
}

/**
 * Represents user's current role.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
@UndefinableEnum
enum class Role(val key: Int, val authorities: Collection<GrantedAuthority>) {
    ANONYMOUS(1, Collections.singleton(AUTHORITY_ANONYMOUS)),
    USER(10, Collections.singleton(AUTHORITY_USER)),
    PREMIUM_USER(20, Collections.unmodifiableCollection(setOf(AUTHORITY_USER, AUTHORITY_PREMIUM_USER))),
    MODERATOR(30, Collections.unmodifiableCollection(setOf(AUTHORITY_USER, AUTHORITY_MODERATOR))),
    ADMINISTRATOR(100, Collections.unmodifiableCollection(setOf(AUTHORITY_USER, AUTHORITY_MODERATOR, AUTHORITY_ADMIN))),
    UNDEFINED(-1, Collections.emptyList());

    fun isAnonymous(): Boolean = this == ANONYMOUS

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): Int {
        return key
    }

    companion object {
        fun of(role: String): Role = try {
            Role.valueOf(role)
        } catch (e: IllegalArgumentException) {
            UNDEFINED
        }

        fun getAuthorityOf(authority: String): GrantedAuthority =
                AUTHORITY_BY_ROLE[authority] ?: AUTHORITY_ANONYMOUS

        @JsonCreator
        @JvmStatic
        fun byKey(key: Int?) = Role.values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}
