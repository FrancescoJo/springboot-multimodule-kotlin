/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint

import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcNow

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class AbstractResponseDto<T>(val type: Type) {
    abstract val body: T

    val timestamp: Long = utcNow().utcEpochSecond()

    companion object {
        fun <T> ok(payload: T) = OkResponseDto(payload)

        fun error(message: String, reason: String = "") = ErrorResponseDto(
                ErrorResponseDto.Body(message, reason)
        )
    }

    enum class Type {
        OK,
        ERROR
    }
}
