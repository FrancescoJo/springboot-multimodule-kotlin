/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.jwt

import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.vo.account.AccessToken
import org.springframework.security.core.Authentication
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
class JwtAccessTokenBusinessImpl(

) : AccessTokenBusiness {
    override fun findFromRequest(httpRequest: HttpServletRequest): AccessToken? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(user: User, timestamp: LocalDateTime): AccessToken {


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parse(token: String): AccessToken {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validate(token: AccessToken): Authentication {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
