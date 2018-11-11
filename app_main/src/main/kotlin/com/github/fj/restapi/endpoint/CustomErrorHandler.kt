/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.AppProfile
import com.github.fj.restapi.BuildConfig
import com.github.fj.restapi.exception.AbstractBaseHttpException
import com.github.fj.restapi.exception.GeneralHttpException
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
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
@AllOpen
@Controller
@RestControllerAdvice
class CustomErrorHandler : ErrorController {
    @ExceptionHandler(AuthenticationException::class)
    fun handleSpring401(req: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        return handleError(req, GeneralHttpException.create(HttpStatus.UNAUTHORIZED, req.requestURI ?: ""))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleSpring404(req: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        return handleError(req, GeneralHttpException.create(HttpStatus.NOT_FOUND, req.requestURI ?: ""))
    }

    @ExceptionHandler(Exception::class)
    fun handleError(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponseDto> {
        val status: HttpStatus
        val response = when (ex) {
            is AbstractBaseHttpException -> {
                logError("Handled exception:", ex)
                status = ex.httpStatus
                AbstractResponseDto.error(ex.message, ex.reason)
            }
            is HttpMessageNotReadableException -> {
                logError("Spring handled exception:", ex)
                status = HttpStatus.BAD_REQUEST
                AbstractResponseDto.error("Cannot process given request.", "Malformed request message")
            }
            is JsonProcessingException -> {
                logError("JSON parsing exception:", ex)
                status = HttpStatus.BAD_REQUEST
                AbstractResponseDto.error("Cannot process given request.", "Malformed JSON")
            }
            is MethodArgumentNotValidException -> {
                logError("Validation failure exception:", ex)
                status = HttpStatus.BAD_REQUEST
                val reason = ex.bindingResult.globalError?.defaultMessage
                        ?.takeIf { it.isNotEmpty() } ?: ""
                AbstractResponseDto.error("Cannot process given request.", reason)
            }
            else -> {
                if (ex.cause is Exception) {
                    return handleError(req, ex.cause as Exception)
                } else {
                    // is Exception is wrapped?
                    logError("Unhandled exception:", ex)
                    status = getStatus(req)
                    AbstractResponseDto.error("Unhandled internal server error")
                }
            }
        }

        return ResponseEntity(response, status)
    }

    /**
     * Any errors, that happen in the outside of Spring Context - exceptions in Servlet filters
     * for example, are redirected to this method to decorate error output as our own favour,
     * rather than Spring's [org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController].
     */
    @RequestMapping(BASIC_ERROR_PATH)
    fun handleError(request: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        val exception = request.getAttribute("javax.servlet.error.exception") as? Exception
                ?: GeneralHttpException.create(HttpStatus.BAD_REQUEST)

        return handleError(request, exception)
    }

    override fun getErrorPath(): String {
        return BASIC_ERROR_PATH
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        return (request.getAttribute("javax.servlet.error.status_code") as? Int)?.let {
            HttpStatus.valueOf(it)
        } ?: HttpStatus.BAD_REQUEST
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
                LOG.error("  by {}", cause)
            } else {
                LOG.error("  by P{", cause::class)
                logCauses(cause.cause)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomErrorHandler::class.java)

        private const val BASIC_ERROR_PATH = "/error"
    }
}
