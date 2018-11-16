/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.exception

import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class AbstractBaseHttpException(override val message: String = "",
                                         cause: Throwable? = null) : Exception(message, cause) {
    abstract val httpStatus: HttpStatus

    val reason: String
        get() = this::class.java.simpleName
}
