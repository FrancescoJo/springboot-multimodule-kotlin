/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.helper.validation

import org.springframework.validation.Errors
import org.springframework.validation.Validator

/**
 * Validation:
 * https://docs.spring.io/spring/docs/5.1.1.RELEASE/spring-framework-reference/core.html#validation
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2018
 */
interface NullsafeValidator<T> : Validator {
    override fun validate(target: Any?, errors: Errors) {
        if (target == null) {
            ValidationFailures.rejectNull(errors)
            return
        }

        val convertedTarget: T
        try {
            @Suppress("UNCHECKED_CAST")
            convertedTarget = target as T
        } catch (e: ClassCastException) {
            ValidationFailures.rejectCastError(errors)
            return
        }

        validateNonNull(convertedTarget, errors)?.rejectWith(errors, target)
    }

    fun validateNonNull(target: T, e: Errors): ValidationFailures?
}
