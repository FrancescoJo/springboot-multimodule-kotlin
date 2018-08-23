/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
data class ErrorResponse(override val body: Body) : Response(Type.ERROR) {
    data class Body(val message: String, val reason: String)
}
