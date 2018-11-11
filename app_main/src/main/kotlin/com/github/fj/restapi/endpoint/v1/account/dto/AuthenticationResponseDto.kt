/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.entity.User
import io.seruco.encoding.base62.Base62
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@ApiModel(description = "Represents successful account creation / log in.")
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthenticationResponseDto(
        @ApiModelProperty("Your login method. b: BASIC, g: GUEST", example = "b", required = true)
        @JsonProperty
        val loginType: LoginType,

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

        @ApiModelProperty("An UNIX timestamp that indicates user's last activity.", example = "2018-10-27T00:00:01", required = true)
        @JsonProperty
        val lastActive: LocalDateTime,

        @ApiModelProperty("Access token to authorise user identity. " +
                "Clients must store this information in a secure storage and keep it secret" +
                "as best as possible.", example = "<ENCODED TEXT>", required = true)
        @JsonProperty
        val accessToken: ProtectedProperty<String>,

        @ApiModelProperty("An UNIX timestamp that will appear if a user account is " +
                "banned or suspended in certain timespan. Field may not found or the value will be 0" +
                "if your account status is good.", example = "<UNIX TIMESTAMP>", required = false)
        @JsonProperty
        val suspendedOn: LocalDateTime?,

        @ApiModelProperty("An UNIX timestamp that will appear if a user account is " +
                "banned or suspended in certain timespan. Field may not found or the value will be 0" +
                "if your account status is good.", example = "<UNIX TIMESTAMP>", required = false)
        @JsonProperty
        val suspendedUntil: LocalDateTime?
) {
    companion object {
        fun create(user: User) = AuthenticationResponseDto(
                loginType = user.loginType,
                id = user.idToken,
                nickname = user.member.nickname,
                gender = user.member.gender,
                status = user.status,
                lastActive = user.member.lastActiveTimestamp ?: LOCAL_DATE_TIME_MIN,
                accessToken = ProtectedProperty(Base62.createInstance().encode(user.rawAccessToken)
                        .toString(Charsets.UTF_8)),
                suspendedOn = user.member.suspendedOn,
                suspendedUntil = user.member.suspendedUntil
        )
    }
}
