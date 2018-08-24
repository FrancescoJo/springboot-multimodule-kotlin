/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.ResponseDto
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@RequestMapping(path = ["/hello"],
        produces = ["application/json"],
        consumes = ["application/json"])
@RestController
class HelloController {
    @RequestMapping(method = [RequestMethod.GET])
    fun onGet(): ResponseDto<*> {
        return HelloResponseDto.create("GET Hello, world")
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun onPost(@RequestBody request: HelloRequestDto): ResponseDto<*> {
        return HelloResponseDto.create("POST Hello, ${request.name}")
    }
}
