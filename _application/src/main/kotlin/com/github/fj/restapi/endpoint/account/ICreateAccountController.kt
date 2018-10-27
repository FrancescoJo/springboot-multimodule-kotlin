/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.account

import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.hello.HelloResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Oct - 2018
 */
@Api(value = "CreateAccount", description = "Creates an account for service usage.")
@RequestMapping(path = ["/${ApiPaths.ACCOUNT}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
interface ICreateAccountController {
    @ApiOperation("Create account",
            notes = "Creates account for service usage with initially provided information.",
            response = HelloResponseDto::class)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 400, message = "If request is malformed, nickname is not permitted or already exists."),
            ApiResponse(code = 403, message = "If an account with same identity is already created"))
    @RequestMapping(method = [RequestMethod.POST])
    fun onPost(@Valid @RequestBody request: CreateAccountRequestDto,
               httpServletRequest: HttpServletRequest): AuthenticationResponseDto
}
