/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@ApiModel(description = "Hello response representation")
@JsonSerialize
data class HelloResponseDto(
        @ApiModelProperty(example = "Hello world, John Doe", required = true)
        @JsonProperty
        val message: String = ""
)
