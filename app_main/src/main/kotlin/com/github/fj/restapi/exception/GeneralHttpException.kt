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
class GeneralHttpException private constructor(status: HttpStatus,
                                               message: String = "", cause: Throwable? = null) :
        AbstractBaseHttpException(message, cause) {
    override val httpStatus = status

    companion object {
        fun create(httpStatus: HttpStatus, resourceName: String = "", cause: Throwable? = null) =
                if (resourceName.isEmpty()) {
                    GeneralHttpException(httpStatus, httpStatus.reasonPhrase, cause)
                } else {
                    GeneralHttpException(httpStatus, httpStatus.reasonPhrase + ": $resourceName",
                            cause)
                }
    }
}
