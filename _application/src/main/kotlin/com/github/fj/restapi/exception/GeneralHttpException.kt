/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.exception

import org.springframework.http.HttpStatus

/**
 * This class represents a general HTTP exception. However, it is discouraged to use only this
 * class for exception handling. Elaborate errors as much as possible.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
class GeneralHttpException private constructor(httpCode: Int,
                                               message: String = "", cause: Throwable? = null) :
        AbstractBaseException(message, cause) {
    override val httpStatusCode = httpCode

    companion object {
        fun create(httpStatus: HttpStatus, resourceName: String = "", cause: Throwable? = null) =
                if (resourceName.isEmpty()) {
                    GeneralHttpException(httpStatus.value(), httpStatus.reasonPhrase, cause)
                } else {
                    GeneralHttpException(httpStatus.value(), httpStatus.reasonPhrase + ": $resourceName", cause)
                }
    }
}
