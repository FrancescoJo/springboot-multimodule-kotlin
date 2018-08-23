/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class Response(val type: Type) {
    abstract val body: Any

    companion object {
        fun ok(payload: Any) = OkResponse(payload)

        fun error(message: String, reason: String = "") = ErrorResponse(
                ErrorResponse.Body(message, reason)
        )
    }

    enum class Type {
        OK,
        ERROR
    }
}
