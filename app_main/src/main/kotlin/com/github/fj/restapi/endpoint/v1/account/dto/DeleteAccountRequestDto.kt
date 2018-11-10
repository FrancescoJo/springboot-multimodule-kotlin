/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
@ApiModel(description = "Delete account payload. This whole request body can be omitted.")
@JsonSerialize
data class DeleteAccountRequestDto(
        @ApiModelProperty("Elaborated deletion reason if provided by user.",
                example = "I do not want to use this anymore", required = false)
        @JsonProperty
        val reason: String = ""
)
