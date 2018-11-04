package com.github.fj.restapi.appconfig.mvc.security.internal

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetailsServiceImpl: UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        println("LOAD USER BY USERNAME? " + username)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
