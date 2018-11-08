/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.vo.account.AccessToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
class AuthenticationObjectImpl(
        private val user: User,
        private val token: AccessToken
) : Authentication {
    private var isAuthenticated = false

    override fun getAuthorities(): Collection<GrantedAuthority> = user.role.authorities

    override fun getName(): String = user.member.nickname

    override fun getCredentials(): Any = user.credential

    override fun getPrincipal(): AccessToken = token

    override fun getDetails(): User = user

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AuthenticationObjectImpl) {
            return false
        }

        if (this === other) {
            return true
        }

        return Objects.equals(user, other.user) &&
                Objects.equals(token, other.token) &&
                Objects.equals(isAuthenticated, other.isAuthenticated)
    }

    /**
     * [isAuthenticated] is omitted because it is mutable.
     */
    override fun hashCode(): Int = Objects.hash(user, token)

    override fun toString(): String {
        return "AuthenticationObjectImpl(" +
                "userId=${user.id}," +
                "userIdToken=${user.idToken}," +
                "accessToken=$token," +
                "isAuthenticated=$isAuthenticated" +
                ")"
    }
}
