/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.spel

import com.github.fj.restapi.appconfig.mvc.security.internal.AuthenticationObjectImpl
import com.github.fj.restapi.exception.account.UnauthorisedException
import org.springframework.context.ApplicationContext
import org.springframework.expression.Expression
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * A reinvented wheel version of [org.springframework.security.web.access.intercept.FilterSecurityInterceptor].
 * Delegates its actual SPEL evaluation to [org.springframework.expression.spel.support.StandardEvaluationContext].
 *
 * This class is useless if a proper Spring security configuration will have been made.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Nov - 2018
 */
class PreAuthorizeSpelInterceptor(
        private val applicationContext: ApplicationContext
) : HandlerInterceptorAdapter() {
    private val parser = SpelExpressionParser()
    private val exprCache = ConcurrentHashMap<HandlerMethod, Expression>()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication as? AuthenticationObjectImpl
                ?: return true
        val handlerMethod = handler as? HandlerMethod ?: return true

        prehandleInternal(authentication, handlerMethod)
        return true
    }

    private fun prehandleInternal(authentication: AuthenticationObjectImpl, method: HandlerMethod) {
        val preAuthorize = method.getMethodAnnotation(PreAuthorize::class.java) ?: return

        val expression = exprCache[method].let {
            return@let if (it == null) {
                exprCache[method] = parser.parseExpression(preAuthorize.value)
                requireNotNull(exprCache[method])
            } else {
                it
            }
        }

        val spelContext = StandardEvaluationContext(PreAuthorizeSpelRoot(applicationContext, authentication))
        val unauthorised = expression.getValue(spelContext, Boolean::class.java) == true
        if (!unauthorised) {
            throw UnauthorisedException("Access to this resource is denied.")
        }
    }
}
