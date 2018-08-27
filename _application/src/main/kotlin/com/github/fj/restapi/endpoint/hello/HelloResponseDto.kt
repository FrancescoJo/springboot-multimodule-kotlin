/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.OkResponseDto
import com.github.fj.restapi.endpoint.hello.HelloResponseDto.Body

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
data class HelloResponseDto(override val body: Body) : OkResponseDto<Body>(body) {
    // Must have a default constructor in order to use at tests
    data class Body(val message: String = "")

    companion object {
        fun create(message: String) = HelloResponseDto(Body(
                message
        ))
    }
}
