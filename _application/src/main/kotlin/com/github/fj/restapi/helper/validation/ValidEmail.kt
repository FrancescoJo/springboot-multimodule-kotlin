/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.helper.validation

import com.github.fj.restapi.persistence.entity.User
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY,
        AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidEmailConstraint::class])
@MustBeDocumented
annotation class ValidEmail(val message: String = "Not a valid email address",
                            val groups: Array<KClass<*>> = [],
                            val payload: Array<KClass<out Payload>> = [],

                            val fieldName: String = "",
                            val required: Boolean = true)

/**
 * Validates given value conforms the email specification defined in RFC 3696.
 * http://www.rfc-editor.org/errata_search.php?rfc=3696
 */
private class ValidEmailConstraint : ConstraintValidator<ValidEmail, String> {
    private lateinit var fieldName: String
    private lateinit var message: String
    private var required: Boolean = false

    override fun initialize(constraintAnnotation: ValidEmail) = constraintAnnotation.let {
        this.fieldName = it.fieldName
        this.message = it.message
        this.required = it.required
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return if (required && value.isNullOrBlank()) {
            withCustomMessage(context)
        } else if (isValidEmail(value!!)) {
            true
        } else {
            withCustomMessage(context)
        }
    }

    private fun withCustomMessage(context: ConstraintValidatorContext) = with(context) {
        disableDefaultConstraintViolation()
        buildConstraintViolationWithTemplate("$message: $fieldName")
                .addConstraintViolation()

        return@with false
    }

    companion object {
        private const val EMAIL_REGEX = "^[_a-zA-Z0-9-]+([._a-zA-Z0-9-]*)@[a-zA-Z0-9]+(\\.[a-zA-Z0-9-]+)+$"
        private const val EMAIL_LENGTH = User.EMAIL_LENGTH

        private val EMAIL_PATTERN = EMAIL_REGEX.toPattern()

        private fun isValidEmail(email: String): Boolean =
                email.length < EMAIL_LENGTH && EMAIL_PATTERN.matcher(email).matches()
    }
}
