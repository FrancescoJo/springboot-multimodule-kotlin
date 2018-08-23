/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.Response
import org.slf4j.LoggerFactory
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
    fun onGet(): Response {
        return Response.ok("GET Hello, world")
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun onPost(@RequestBody request: HelloRequestDto): Response {
        return Response.ok("POST Hello, ${request.name}")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(HelloController::class.java)
    }
}
