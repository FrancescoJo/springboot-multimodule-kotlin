/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.lib.annotation.AllOpen
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * An authentication success handler which utilises saved HTTP request but skips redirection
 * feature, which is a HTTP authentication standard.
 *
 * See also: [org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler]
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Component
class SavedRequestAwareAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    private var requestCache: RequestCache = HttpSessionRequestCache()

    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse, authentication: Authentication) {
        requestCache.getRequest(request, response).let {
            if (it == null) {
                return super.onAuthenticationSuccess(request, response, authentication)
            }
        }

        targetUrlParameter.let {
            if (isAlwaysUseDefaultTargetUrl || !it.isNullOrBlank() && !request.getParameter(it).isNullOrBlank()) {
                requestCache.removeRequest(request, response)
                return super.onAuthenticationSuccess(request, response, authentication)
            }
        }

        clearAuthenticationAttributes(request)
    }
}
