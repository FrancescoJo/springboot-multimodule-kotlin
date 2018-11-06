/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.service.account.CreateAccountService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@RestController
class CreateAccountController @Inject constructor(private val svc: CreateAccountService) :
        ICreateAccountController {
    override fun onPost(@Valid @RequestBody request: CreateAccountRequestDto,
                        httpServletRequest: HttpServletRequest): AuthenticationResponseDto {
        LOG.debug("Create account request: $request")
        svc.createAccount(request, httpServletRequest).let {
            LOG.debug("Create account response: $it")
            return it
        }
    }

    // On-the-fly validator application
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.addValidators(CreateAccountRequestDto.VALIDATOR)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateAccountController::class.java)
    }
}
