/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.hello.HelloRequestDto
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
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Api(value = "Hello", description = "Hello controller")
@RequestMapping(path = ["/hello"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
interface IHelloController {
    @ApiOperation("Get hello message",
            notes = "The simplest way to get hello message.",
            response = HelloResponseDto::class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"))
    @RequestMapping(method = [RequestMethod.GET])
    fun onGet(): HelloResponseDto

    @ApiOperation("Get hello message with your name",
            notes = "Responses a hello message with your name.",
            response = HelloResponseDto::class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "Successful transaction"),
            ApiResponse(code = 400, message = "Occurs when a request with empty name.")
    )
    @RequestMapping(method = [RequestMethod.POST])
    fun onPost(@Valid @RequestBody request: HelloRequestDto): HelloResponseDto
}
