/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.consts.account

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Nov - 2018
 */
class Authority {
    companion object {
        val AUTHORITY_ANONYMOUS: GrantedAuthority = SimpleGrantedAuthority("ANONYMOUS")
        val AUTHORITY_USER: GrantedAuthority = SimpleGrantedAuthority("USER")
        val AUTHORITY_PREMIUM_USER: GrantedAuthority = SimpleGrantedAuthority("PREMIUM_USER")
        val AUTHORITY_MODERATOR: GrantedAuthority = SimpleGrantedAuthority("MODERATOR")
        val AUTHORITY_ADMIN: GrantedAuthority = SimpleGrantedAuthority("ADMIN")
        val AUTHORITY_BY_ROLE = HashMap<String, GrantedAuthority>().apply {
            put("ANONYMOUS", AUTHORITY_ANONYMOUS)
            put("USER", AUTHORITY_USER)
            put("PREMIUM_USER", AUTHORITY_PREMIUM_USER)
            put("MODERATOR", AUTHORITY_MODERATOR)
            put("ADMIN", AUTHORITY_ADMIN)
        }
    }
}
