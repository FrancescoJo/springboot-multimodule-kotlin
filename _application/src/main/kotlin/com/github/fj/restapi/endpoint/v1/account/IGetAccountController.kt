/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.restapi.dto.account.ProfileResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.entity.User
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Oct - 2018
 */
@Api(value = "Query", description = "Asks an user information represented by given access token.")
@RequestMapping(path = [ApiPaths.API_V1_ACCOUNT],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
interface IGetAccountController {
    @ApiOperation("Query",
            notes = "Retrieve user information, represented by given access token.",
            response = ProfileResponseDto::class)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 401, message = "If given credential was tampered"),
            ApiResponse(code = 403, message = "If user's privilege is not allowed to perform this request."))
//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = [RequestMethod.GET])
    fun onGet(user: User): ProfileResponseDto
}