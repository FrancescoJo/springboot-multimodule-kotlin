/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fj.restapi.dto.ErrorResponseDto
import com.github.fj.restapi.dto.ResponseDto
import com.github.fj.restapi.exception.AbstractBaseException
import com.github.fj.restapi.exception.GeneralHttpException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

/**
 * This only works if following settings are applied:
 * ```
 * spring:
 *   mvc:
 *     throw-exception-if-no-handler-found: true
 *   resources:
 *     add-mappings: false
 * ```
 * Read [this post](https://stackoverflow.com/questions/28902374/spring-boot-rest-service-exception-handling/30193013)
 * for more information.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
@RestControllerAdvice
class CustomErrorHandler {
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleSpring404(req: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        return handleError(req, GeneralHttpException.create(HttpStatus.NOT_FOUND))
    }

    @Suppress("MemberVisibilityCanBePrivate")
    @ExceptionHandler(Exception::class)
    fun handleError(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponseDto> {
        val status: Int
        val response = when (ex) {
            is AbstractBaseException -> {
                LOG.error("Handled exception: ", ex)
                status = ex.httpStatusCode
                ResponseDto.error(ex.message, ex.reason)
            }
            is HttpMessageNotReadableException -> {
                LOG.error("Spring handled exception: ", ex)
                status = HttpStatus.BAD_REQUEST.value()
                ResponseDto.error("Cannot process given request.")
            }
            is JsonProcessingException -> {
                LOG.error("JSON parsing exception: ", ex)
                status = HttpStatus.BAD_REQUEST.value()
                ResponseDto.error("Cannot process given request.")
            }
            else -> {
                LOG.error("Unhandled exception: ", ex)
                status = getStatus(req).value()
                val message = "Unhandled internal server error"
                ResponseDto.error(message)
            }
        }

        return ResponseEntity(response, HttpStatus.valueOf(status))
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        return (request.getAttribute("javax.servlet.error.status_code") as? Int)?.let {
            HttpStatus.valueOf(it)
        } ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomErrorHandler::class.java)
    }
}
