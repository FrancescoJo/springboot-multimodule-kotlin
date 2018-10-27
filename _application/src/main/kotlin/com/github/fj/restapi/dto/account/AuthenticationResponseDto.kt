/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.account

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.Status
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@ApiModel(description = "Represents successful account creation / log in.")
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthenticationResponseDto(
        /**
         * This value must be [com.github.fj.restapi.persistence.entity.User.idToken].
         * It is strongly discouraged to expose numeric primary key to the outside world.
         */
        @ApiModelProperty("An encrypted user Id. Unique value.", example = "AqCzKa7E", required = true)
        @JsonProperty
        val id: String = "",

        @ApiModelProperty("Nickname, as user registered. This value is not unique.", example = "My Nickname", required = true)
        @JsonProperty
        val nickname: String,

        @ApiModelProperty("Gender, as user registered.", example = "m", required = true)
        @JsonProperty
        val gender: Gender,

        @ApiModelProperty("Account status.", example = "n", allowableValues = "n, s, b", required = true)
        @JsonProperty
        val status: Status,

        @ApiModelProperty("An UNIX timestamp that indicates user's last activity.", example = "1540735180000", required = true)
        @JsonProperty
        val lastActiveTimestamp: Long,

        @ApiModelProperty("Access token to authorise user identity. " +
                "Clients must store this information in a secure storage and keep it secret" +
                "as best as possible.", example = "<ENCODED TEXT>", required = true)
        @JsonProperty
        val accessToken: String,

        @ApiModelProperty("An UNIX timestamp that will appear if a user account is " +
                "banned or suspended in certain timespan. Field may not found or the value will be 0" +
                "if your account status is good.", example = "<UNIX TIMESTAMP>")
        @JsonProperty
        val suspendedOnTimestamp: Long,

        @ApiModelProperty("An UNIX timestamp that will appear if a user account is " +
                "banned or suspended in certain timespan. Field may not found or the value will be 0" +
                "if your account status is good.", example = "<UNIX TIMESTAMP>")
        @JsonProperty
        val suspendedUntilTimestamp: Long
)
