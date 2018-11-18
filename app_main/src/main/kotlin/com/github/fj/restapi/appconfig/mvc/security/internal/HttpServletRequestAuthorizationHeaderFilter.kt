/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.lib.text.matchesIn
import com.github.fj.restapi.component.auth.HttpAuthorizationToken
import com.github.fj.restapi.component.auth.HttpAuthScheme
import org.slf4j.Logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This filter attempts to locate `Authorization` header in user request and parses it via
 * authorisation types and saves its result into [org.springframework.security.core.context.SecurityContextHolder].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class HttpServletRequestAuthorizationHeaderFilter(
        private val log: Logger,
        private val excludeMatchers: List<RequestMatcher>
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as? HttpServletRequest
        val resp = response as? HttpServletResponse
        if (req == null || resp == null) {
            log.w("Cannot accept requests other than HttpServletRequest.")
            chain.doFilter(request, response)
            return
        }

        if (excludeMatchers.any { it.matches(request) }) {
            log.t("This request does not seems requiring any authentication.")
            chain.doFilter(req, resp)
            return
        }

        val token = findAuthorizationHeader(req, log)
        if (token != null) {
            log.t("HTTP Authorization header has been found with: ${token.scheme} scheme")
            SecurityContextHolder.getContext().authentication = token
        }

        chain.doFilter(req, resp)
    }

    companion object {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        const val HEADER_AUTHORIZATION = "Authorization"

        private val AUTHORIZATION_SYNTAX = Pattern.compile("[A-Za-z]+ [A-Za-z0-9]+")

        fun findAuthorizationHeader(req: HttpServletRequest, log: Logger? = null):
                HttpAuthorizationToken? {
            req.getHeader(HEADER_AUTHORIZATION).let { h ->
                when {
                    h.isNullOrEmpty() -> log?.t(
                            "No {} header in the request.", HEADER_AUTHORIZATION)
                    !h.matchesIn(AUTHORIZATION_SYNTAX) -> log?.t(
                            "{} header does not match the syntax: '{}'", HEADER_AUTHORIZATION, h)
                    else -> return h.split(" ").let {
                        HttpAuthorizationToken(HttpAuthScheme.byTypeValue(it[0]), it[1])
                    }
                }
                return null
            }
        }

        private fun Logger.t(message: String, vararg arguments: Any) {
            trace(String.format("[RequestFilter] %s", message), arguments)
        }

        private fun Logger.w(message: String, vararg arguments: Any) {
            warn(String.format("[RequestFilter] %s", message), arguments)
        }
    }
}
