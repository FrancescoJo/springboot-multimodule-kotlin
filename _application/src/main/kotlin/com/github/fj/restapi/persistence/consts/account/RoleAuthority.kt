/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Set of authorities defined for user role.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
internal class RoleAuthority {
    companion object {
        val ROLE_ANONYMOUS: GrantedAuthority = SimpleGrantedAuthority("ANONYMOUS")
        val ROLE_USER: GrantedAuthority = SimpleGrantedAuthority("USER")
        val ROLE_PREMIUM_USER: GrantedAuthority = SimpleGrantedAuthority("PREMIUM_USER")
        val ROLE_MODERATOR: GrantedAuthority = SimpleGrantedAuthority("MODERATOR")
        val ROLE_ADMIN: GrantedAuthority = SimpleGrantedAuthority("ADMIN")
    }
}
