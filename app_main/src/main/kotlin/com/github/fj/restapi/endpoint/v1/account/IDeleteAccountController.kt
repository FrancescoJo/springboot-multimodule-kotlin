/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.restapi.appconfig.aop.LoggedActivity
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountRequestDto
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.UserActivity
import com.github.fj.restapi.persistence.entity.User
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Oct - 2018
 */
@Api(value = "DeleteAccount", description = "Deletes owning account and abandon all service usage history.")
@RequestMapping(path = [ApiPaths.API_V1_ACCOUNT],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
interface IDeleteAccountController {
    @ApiOperation("Delete Account",
            notes = "Deletes all user information those are required for service usage.",
            response = DeleteAccountResponseDto::class)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 400, message = "If request is malformed"),
            ApiResponse(code = 401, message = "If given credential was tampered"))
//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = [RequestMethod.DELETE])
    @LoggedActivity(UserActivity.DELETE_ACCOUNT)
    @ResponseBody
    fun onDelete(user: User, @RequestBody deleteReason: DeleteAccountRequestDto?): DeleteAccountResponseDto
}
