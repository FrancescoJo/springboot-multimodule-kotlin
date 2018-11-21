/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.exception.account.UserNotFoundException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
interface LoginService {
    @Throws(UserNotFoundException::class)
    fun guestLogin(accessToken: String, request: LoginRequestDto): AuthenticationResponseDto

    @Throws(UserNotFoundException::class)
    fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto
}
