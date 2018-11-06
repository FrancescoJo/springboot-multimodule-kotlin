/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.LoginRequestDto
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.service.account.LoginService
import com.github.fj.restapi.vo.account.AccessToken
import org.slf4j.LoggerFactory
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@RestController
class LoginController @Inject constructor(
        private val loginService: LoginService
) : ILoginController {
    override fun onPatch(accessToken: AccessToken?,
                         @Valid @RequestBody request: LoginRequestDto): AuthenticationResponseDto {
        LOG.debug("Login request: $request")

        return when (request.loginType) {
            LoginType.GUEST -> {
                if (accessToken == null) {
                    throw UnknownAuthTokenException("No access token was found for GUEST login.")
                } else {
                    loginService.guestLogin(accessToken, request)
                }
            }
            LoginType.BASIC -> loginService.basicLogin(request)
            else -> throw UnsupportedOperationException("Login type is not supported.")
        }
    }

    // On-the-fly validator application
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.addValidators(LoginRequestDto.VALIDATOR)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LoginController::class.java)
    }
}
