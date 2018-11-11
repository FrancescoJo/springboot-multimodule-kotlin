package com.github.fj.restapi.exception

import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
open class AuthTokenException(override val message: String = "", cause: Throwable? = null,
                              override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED)
    : AbstractBaseHttpException(message, cause)
