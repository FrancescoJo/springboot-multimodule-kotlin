/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.helper.validation

import org.slf4j.LoggerFactory
import org.springframework.validation.Errors

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2018
 */
enum class ValidationFailures constructor(private val code: String) {
    VALUE_INSUFFICIENT("ERR_VALIDATION_VAL_INSUFFICIENT"),
    VALUE_UNNECESSARY("ERR_VALIDATION_VAL_UNNECESSARY"),
    VALUE_INVALID_RANGE("ERR_VALIDATION_VAL_INVALID_RANGE");

    fun rejectWith(e: Errors, obj: Any?) {
        val message: String = when (this) {
            VALUE_INSUFFICIENT -> "Some property is/are missing for fulfill the request."
            VALUE_INVALID_RANGE -> "Value is out of range."
            VALUE_UNNECESSARY -> "Unnecessary extra value(s) is/are found."
        }

        if (obj != null) {
            LOG.debug("Error cause: {}", obj)
        }
        e.reject(code, message)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ValidationFailures::class.java)

        internal fun rejectNull(e: Errors) {
            rejectInternal(e, "ERR_VALIDATION_VAL_NULL_EMPTY", "Value is null or empty.")
        }

        internal fun rejectCastError(e: Errors) {
            rejectInternal(e, "ERR_VALIDATION_CAST_ERROR", "Type conversion error.")
        }

        private fun rejectInternal(e: Errors, code: String, message: String) {
            e.reject(code, message)
        }
    }
}
