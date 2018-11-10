/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint

import com.github.fj.restapi.endpoint.ErrorResponseDto.Body

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
data class ErrorResponseDto(override val body: Body) : AbstractResponseDto<Body>(Type.ERROR) {
    data class Body(val message: String, val reason: String)
}
