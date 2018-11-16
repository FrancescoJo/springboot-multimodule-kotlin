/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.restapi.persistence.consts.account.Gender
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@ApiModel(description = "Holds basic profile of authorised user.")
@JsonSerialize
data class ProfileResponseDto(
    @ApiModelProperty("An encrypted user id, can be shared across users.",
            example = "ATF4QL92ZW350B6F.", required = true)
    @JsonProperty
    val id: String,

    @ApiModelProperty("Membership level.", example = "10", required = true)
    @JsonProperty
    val membershipLevel: Int,

    @ApiModelProperty("A date since this user has been joined. Based on UTC clock.",
            example = "2018-11-04T00:00:01", required = true)
    @JsonProperty
    val joinedSince: LocalDateTime,

    @ApiModelProperty("A display name of this user.",
            example = "John doe", required = true)
    @JsonProperty
    val nickname: String,

    @ApiModelProperty("Gender, Empty value or omitting this field means " +
            "your gender will be invisible to other people.", example = "m", required = false,
            allowableValues = "m: MALE, f: FEMALE, \"\": UNDEFINED")
    @JsonProperty
    val gender: Gender?,

    @ApiModelProperty("A last active time this user has been spotted. Maybe null.",
            example = "2018-11-04T00:00:01", required = false)
    @JsonProperty
    val lastActive: LocalDateTime?,

    @ApiModelProperty("Suspended time since, if user has been suspended. Maybe null.",
            example = "2018-11-04T00:00:01", required = false)
    @JsonProperty
    var suspendedOn: LocalDateTime?,

    @ApiModelProperty("Suspended time until, if user has been suspended. Maybe null.",
            example = "2018-11-04T00:00:01", required = false)
    @JsonProperty
    var suspendedUntil: LocalDateTime?
)
