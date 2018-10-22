/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fj.restapi.AppProfile
import com.github.fj.restapi.BuildConfig
import com.github.fj.restapi.dto.ErrorResponseDto
import com.github.fj.restapi.dto.AbstractResponseDto
import com.github.fj.restapi.exception.AbstractBaseException
import com.github.fj.restapi.exception.GeneralHttpException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
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
                logError("Handled exception:", ex)
                status = ex.httpStatusCode
                AbstractResponseDto.error(ex.message, ex.reason)
            }
            is HttpMessageNotReadableException -> {
                logError("Spring handled exception:", ex)
                status = HttpStatus.BAD_REQUEST.value()
                AbstractResponseDto.error("Cannot process given request.", "Malformed request message")
            }
            is JsonProcessingException -> {
                logError("JSON parsing exception:", ex)
                status = HttpStatus.BAD_REQUEST.value()
                AbstractResponseDto.error("Cannot process given request.", "Malformed JSON")
            }
            is MethodArgumentNotValidException -> {
                logError("Validation failure exception:", ex)
                status = HttpStatus.BAD_REQUEST.value()
                val reason = ex.bindingResult.globalError?.defaultMessage
                        ?.takeIf { it.isNotEmpty() } ?: ""
                AbstractResponseDto.error("Cannot process given request.", reason)
            }
            else -> {
                logError("Unhandled exception:", ex)
                status = getStatus(req).value()
                AbstractResponseDto.error("Unhandled internal server error")
            }
        }

        return ResponseEntity(response, HttpStatus.valueOf(status))
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        return (request.getAttribute("javax.servlet.error.status_code") as? Int)?.let {
            HttpStatus.valueOf(it)
        } ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    private fun logError(message: String, ex: Exception) {
        if (BuildConfig.currentProfile == AppProfile.RELEASE) {
            // Minimise log outputs in RELEASE binary
            LOG.error(message)
            logCauses(ex)
        } else {
            LOG.error(message, ex)
        }
    }

    private fun logCauses(cause: Throwable?) {
        if (cause == null) {
            return
        } else {
            if (cause.cause == null) {
                LOG.error("  by $cause")
            } else {
                LOG.error("  by ${cause::class}")
                logCauses(cause.cause)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomErrorHandler::class.java)
    }
}
