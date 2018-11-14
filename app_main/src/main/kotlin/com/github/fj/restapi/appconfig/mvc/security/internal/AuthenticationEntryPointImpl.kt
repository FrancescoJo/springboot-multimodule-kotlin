/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.lib.annotation.AllOpen
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Although this class is named as "Entry point", the concept "Entry point" does not hold
 * any meaning of "Request entry point", it's rather than focused on postprocessing
 * request success or failures.
 *
 * Read [org.springframework.security.web.authentication.www.BasicAuthenticationFilter]
 * for its true concept.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Component
class AuthenticationEntryPointImpl : AuthenticationEntryPoint {
    override fun commence(req: HttpServletRequest, resp: HttpServletResponse,
                          e: AuthenticationException?) {
        // Let fail all authentication requests where authorisation exception has been raised.
        resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
    }
}
