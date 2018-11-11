/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc

import com.github.fj.restapi.util.extractIpStr
import com.google.common.base.Strings
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Logs all requests in our favourite fashion.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 */
@Component
class RequestLoggingInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val ip     = request.run { "${extractIpStr()} ($remoteAddr:$remotePort)" }
        val method = Strings.padEnd(request.method, 6, ' ')
        val path   = request.servletPath
        val handlerInfo = if (handler is HandlerMethod) {
            val hm = handler.method
            val className  = hm.declaringClass.canonicalName
            val methodName = hm.name
            String.format("%s#%s", className, methodName)
        } else {
            handler.javaClass.toString()
        }

        LOG.info("{} {} from {} << {}", method, path, ip, handlerInfo)
        val headers = request.headerNames

        while(headers.hasMoreElements()) {
            val header = headers.nextElement()
            val value = request.getHeader(header)
            LOG.debug("{} : {}", header, value)
        }

        return super.preHandle(request, response, handler)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)
    }
}
