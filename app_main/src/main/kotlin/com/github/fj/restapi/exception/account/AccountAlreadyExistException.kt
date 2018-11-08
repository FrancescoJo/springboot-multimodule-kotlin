/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.exception.account

import com.github.fj.restapi.exception.AbstractBaseHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Oct - 2018
 */
class AccountAlreadyExistException(override val message: String = "", cause: Throwable? = null,
                                   override val httpStatus: HttpStatus = HttpStatus.FORBIDDEN)
    : AbstractBaseHttpException(message, cause)