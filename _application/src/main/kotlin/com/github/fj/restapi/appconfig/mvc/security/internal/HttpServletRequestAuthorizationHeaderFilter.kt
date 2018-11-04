/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.lib.text.matchesIn
import com.github.fj.restapi.appconfig.mvc.security.HttpAuthScheme
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * This filter attempts to locate `Authorization` header in user request and parses it via
 * authorisation types and saves its result into [org.springframework.security.core.context.SecurityContextHolder].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class HttpServletRequestAuthorizationHeaderFilter(private val log: Logger) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = findAuthorizationHeader(log, request)
        if (token != null) {
            SecurityContextHolder.getContext().authentication = token
        }

        chain.doFilter(request, response)
    }

    companion object {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        const val HEADER_AUTHORIZATION = "Authorization"

        private val AUTHORIZATION_SYNTAX = Pattern.compile("[A-Za-z]+ [A-Za-z0-9]+")

        fun findAuthorizationHeader(log: Logger, request: ServletRequest): HttpAuthorizationToken? {
            if (request !is HttpServletRequest) {
                log.t("Not a HTTP request: {}", request)
                return null
            }

            return request.getHeader(HEADER_AUTHORIZATION).let { h ->
                if (h.isNullOrEmpty()) {
                    log.t("No {} header in the request.", HEADER_AUTHORIZATION)
                    return null
                }

                if (!h.matchesIn(AUTHORIZATION_SYNTAX)) {
                    log.t("{} header does not match the syntax: '{}'", HEADER_AUTHORIZATION, h)
                    return null
                }

                return@let h.split(" ").let {
                    HttpAuthorizationToken(HttpAuthScheme.byTypeValue(it[0]), it[1])
                }
            }
        }

        private fun Logger.t(message: String, vararg arguments: Any) {
            trace(String.format("[RequestFilter] %s", message), arguments)
        }
    }
}
