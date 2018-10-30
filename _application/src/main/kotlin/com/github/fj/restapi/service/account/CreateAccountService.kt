/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
interface CreateAccountService {
    fun createAccount(req: CreateAccountRequestDto, httpReq: HttpServletRequest):
            AuthenticationResponseDto
}
