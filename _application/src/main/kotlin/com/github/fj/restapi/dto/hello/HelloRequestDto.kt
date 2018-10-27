/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.hello

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.util.EmptyObject
import com.github.fj.restapi.helper.validation.NullsafeValidator
import com.github.fj.restapi.helper.validation.ValidationFailures
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors
import org.springframework.validation.Validator

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@ApiModel(description = "Hello request payload")
@JsonDeserialize
data class HelloRequestDto @JvmOverloads constructor(
        @ApiModelProperty(example = "John Doe", required = true)
        @JsonProperty
        val name: String = ""
) {
    companion object : EmptyObject<HelloRequestDto> {
        override val EMPTY = HelloRequestDto("")

        val VALIDATOR = object : NullsafeValidator<HelloRequestDto> {
            override fun supports(klass: Class<*>) =
                    HelloRequestDto::class.java.isAssignableFrom(klass)

            override fun validateNonNull(target: HelloRequestDto, e: Errors): ValidationFailures? =
                    with(target) {
                        if (name.isEmpty()) {
                            e.reject("UNEXPECTED_VALUE", "name must not be empty.")
                        }

                        return@with null
                    }
        }
    }
}
