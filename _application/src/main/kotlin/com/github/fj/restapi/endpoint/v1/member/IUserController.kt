/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.member

import com.github.fj.restapi.dto.v1.member.UserResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Api(value = "User", description = "User controller only for authorised users")
@RequestMapping("/${ApiPaths.API_V1}/user")
interface IUserController {
    @ApiOperation("Gets a user information.",
            notes = "Returns user information with their role.",
            response = UserResponseDto::class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 401, message = "Authentication failure"))
    @RequestMapping(method = [RequestMethod.GET])
    fun onGetUser(): UserResponseDto
}
