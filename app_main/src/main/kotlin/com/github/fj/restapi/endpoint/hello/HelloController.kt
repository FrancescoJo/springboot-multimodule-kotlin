/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.hello.HelloRequestDto
import com.github.fj.restapi.dto.hello.HelloResponseDto
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@RestController
class HelloController : IHelloController {
    override fun onGet(): HelloResponseDto {
        return HelloResponseDto("GET Hello, world")
    }

    override fun onPost(@Valid @RequestBody request: HelloRequestDto): HelloResponseDto {
        return HelloResponseDto("POST Hello, ${request.name}")
    }

    // On-the-fly validator application
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.addValidators(HelloRequestDto.VALIDATOR)
    }
}
