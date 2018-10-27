/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security

import com.github.fj.lib.annotation.AllOpen
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Component
class AuthenticationEntryPointImpl : AuthenticationEntryPoint {
    override fun commence(req: HttpServletRequest, resp: HttpServletResponse,
                          e: AuthenticationException?) {
        // TODO: Watch this logic whether it still required after AuthenticationManager delegation
        println("AuthenticationEntryPointConfig: -- commence")
        e?.printStackTrace()

        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}
