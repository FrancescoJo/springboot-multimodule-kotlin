/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.svc.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Referenced from [Spring boot issues page](https://github.com/spring-projects/spring-boot/issues/3980).
 * Workaround about `@ControllerAdvice` utilisation does not work on REST applications
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@RestController
@RequestMapping("\${error.path:/error}")
class CustomErrorController : ErrorController {
    @Value("\${error.path:/error}")
    private val errorPath: String? = null

    override fun getErrorPath(): String? {
        return this.errorPath
    }

    @RequestMapping
    fun onError(ex: Exception, request: HttpServletRequest): ResponseEntity<String> {
        val status = getStatus(request)
        return ResponseEntity("Error occurred.", status)
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        return (request.getAttribute("javax.servlet.error.status_code") as? Int)?.let {
            HttpStatus.valueOf(it)
        } ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}
