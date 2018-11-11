/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.exception.account

import com.github.fj.restapi.exception.AuthTokenException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2018
 */
class AuthTokenExpiredException(override val message: String = "", cause: Throwable? = null) :
        AuthTokenException(message, cause)
