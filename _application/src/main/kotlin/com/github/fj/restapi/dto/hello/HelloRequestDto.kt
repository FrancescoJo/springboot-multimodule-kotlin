/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.hello

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.util.EmptyObject
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors
import org.springframework.validation.Validator

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@ApiModel(description = "Hello request representation")
@JsonDeserialize
data class HelloRequestDto constructor(
        @ApiModelProperty(example = "John Doe", required = true)
        @JsonProperty
        val name: String = ""
) {
    companion object : EmptyObject<HelloRequestDto> {
        override val EMPTY = HelloRequestDto("")

        val VALIDATOR = object : Validator {
            // Validation:
            // https://docs.spring.io/spring/docs/5.1.1.RELEASE/spring-framework-reference/core.html#validation
            override fun supports(klass: Class<*>) =
                    HelloRequestDto::class.java.isAssignableFrom(klass)

            override fun validate(target: Any?, e: Errors) = with(target as HelloRequestDto) {
                if (name.isEmpty()) {
                    e.reject("UNEXPECTED_VALUE", "name must not be empty.")
                }
            }
        }
    }
}
