/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
interface LoginService {
    fun guestLogin(accessToken: String, request: LoginRequestDto): AuthenticationResponseDto

    fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto
}
