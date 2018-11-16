/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.helper.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY,
        AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProhibitedWordsConstraint::class])
@MustBeDocumented
annotation class RejectProhibitedWords(val fieldName: String = "",
                                       val message: String = "Contains prohibited words",
                                       val required: Boolean = true)

private class ProhibitedWordsConstraint : ConstraintValidator<RejectProhibitedWords, String> {
    private lateinit var fieldName: String
    private lateinit var message: String
    private var required: Boolean = false

    override fun initialize(constraint: RejectProhibitedWords) = constraint.let {
        this.fieldName = it.fieldName
        this.message = it.message
        this.required = it.required
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (required && value == null) {
            return withCustomMessage(context)
        }

        // Better implementations maybe come later
        return true
    }

    private fun withCustomMessage(context: ConstraintValidatorContext) = with(context) {
        disableDefaultConstraintViolation()
        buildConstraintViolationWithTemplate("$message: $fieldName")
                .addConstraintViolation()

        return@with false
    }
}
