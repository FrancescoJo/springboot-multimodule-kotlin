/*
 * Copyright 2018 Hyperconnect inc. All rights reserved.
 *
 * Hyperconnect inc. intellectual property.
 * Use of this software is subject to licence terms.
 */
package com.github.fj.restapi.endpoint.hello

import com.github.fj.restapi.dto.OkResponseDto
import com.github.fj.restapi.endpoint.hello.HelloResponseDto.Body

/**
 * @author Hwan Jo(hwan.cho@hpcnt.com)
 * @since 27 - Aug - 2018
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
