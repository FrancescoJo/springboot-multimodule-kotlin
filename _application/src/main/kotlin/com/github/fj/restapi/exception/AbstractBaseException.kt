/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.exception

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class AbstractBaseException(override val message: String = "", cause: Throwable? = null) :
        Exception(message, cause) {
    abstract val httpStatusCode: Int

    val reason: String
        get() = this::class.java.simpleName
}
