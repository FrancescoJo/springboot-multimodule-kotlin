/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security.internal

import com.github.fj.lib.annotation.AllOpen
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Component
class AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler()
