/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.restapi.appconfig.aop.LoggedActivity
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.LoginRequestDto
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.UserActivity
import com.github.fj.restapi.vo.account.AccessToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Oct - 2018
 */
@Api(value = "Login", description = "Asks an authentication challenge for access token. For guest " +
        "users, last issued access token, which acts as password, is also required for re-authentication.")
@RequestMapping(path = [ApiPaths.API_V1_ACCOUNT],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
interface ILoginController {
    @ApiOperation("Log in",
            notes = "Retrieve an access token for service usage by exchanging genuine credential.",
            response = AuthenticationResponseDto::class)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 400, message = "If request is malformed"),
            ApiResponse(code = 401, message = "If given credential was tampered"),
            ApiResponse(code = 403, message = "If given credential was rejected by third party SSO providers"))
    @RequestMapping(method = [RequestMethod.PATCH])
    @LoggedActivity(UserActivity.LOG_IN)
    @ResponseBody
    fun onPatch(accessToken: AccessToken?,
                @Valid @RequestBody request: LoginRequestDto): AuthenticationResponseDto
}
