/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.util.EmptyObject
import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.restapi.helper.validation.NullsafeValidator
import com.github.fj.restapi.helper.validation.ValidationFailures
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import com.github.fj.restapi.persistence.entity.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@ApiModel(description = "Login request payload")
@JsonDeserialize
data class LoginRequestDto(
        @ApiModelProperty("A user name, must not be empty and must be ${User.MINIMUM_NAME_LENGTH} - " +
                "${User.MAXIMUM_NAME_LENGTH} alphanumeric letters long if the the loginType is " +
                "BASIC. Maybe empty or omitted if you are using another login type.",
                example = "Good Username", required = false)
        @JsonProperty
        val username: String = "",

        @ApiModelProperty("Could be a password, 3rd party SSO access token, etc.", example = "", required = true)
        @JsonProperty
        val credential: ProtectedProperty<String> = ProtectedProperty(""),

        @ApiModelProperty("Login type. Read document for supported login types.", example = "b", required = true)
        @JsonProperty
        val loginType: LoginType = LoginType.UNDEFINED,

        @ApiModelProperty("Client platform's type.", example = "a", required = true)
        @JsonProperty
        val platformType: PlatformType = PlatformType.UNDEFINED,

        @ApiModelProperty("Client platform's version.", example = "7.0", required = true)
        @JsonProperty
        val platformVersion: String = "",

        @ApiModelProperty("App version that client is currently using.", example = "0.0.1", required = true)
        @JsonProperty
        val appVersion: String = ""
) {
    companion object : EmptyObject<LoginRequestDto> {
        override val EMPTY = LoginRequestDto("")

        val VALIDATOR = object : NullsafeValidator<LoginRequestDto> {
            override fun supports(klass: Class<*>) =
                    LoginRequestDto::class.java.isAssignableFrom(klass)

            override fun validateNonNull(target: LoginRequestDto, e: Errors): ValidationFailures? =
                    with(target) {
                        return@with when {
                            loginType == LoginType.BASIC && (username.isBlank() || credential.value.isBlank()) ->
                                ValidationFailures.VALUE_INSUFFICIENT
                            loginType == LoginType.GUEST && (!username.isBlank() || !credential.value.isBlank()) ->
                                ValidationFailures.VALUE_UNNECESSARY
                            loginType == LoginType.UNDEFINED || platformType == PlatformType.UNDEFINED ||
                                    platformVersion.isEmpty() || appVersion.isEmpty() ->
                                ValidationFailures.VALUE_INSUFFICIENT
                            else -> null
                        }
                    }
        }
    }
}
