/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.LoginRequestDto
import com.github.fj.restapi.vo.account.AccessToken

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
interface LoginService {
    fun guestLogin(accessToken: AccessToken, request: LoginRequestDto): AuthenticationResponseDto

    fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto
}
