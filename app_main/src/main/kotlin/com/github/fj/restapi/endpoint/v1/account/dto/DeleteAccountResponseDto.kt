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
@ApiModel(description = "Represents successful account deletion.")
@JsonSerialize
data class DeleteAccountResponseDto(
        @ApiModelProperty("A good bye message from server.", example = "Goodbye.", required = true)
        @JsonProperty
        val message: String = ""
)
