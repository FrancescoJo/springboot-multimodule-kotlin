/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto.account

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.util.EmptyObject
import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.restapi.helper.validation.NullsafeValidator
import com.github.fj.restapi.helper.validation.ValidEmail
import com.github.fj.restapi.helper.validation.ValidationFailures
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import com.github.fj.restapi.persistence.entity.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@ApiModel(description = "Create request payload")
@JsonDeserialize
data class CreateAccountRequestDto @JvmOverloads constructor(
        @ApiModelProperty("Firebase Cloud Messaging push token.", example = "<FCM PUSH TOKEN>", required = true)
        @JsonProperty
        val pushToken: ProtectedProperty<String> = ProtectedProperty(""),

        @ApiModelProperty("A user name, must not be empty and must be ${User.MINIMUM_NAME_LENGTH} - " +
                "${User.MAXIMUM_NAME_LENGTH} alphanumeric letters long if the the loginType is " +
                "BASIC. Maybe empty or omitted if you are using another login type.",
                example = "Good Username", required = false)
        @JsonProperty
        val username: String = "",

        @ApiModelProperty("Could be a password, 3rd party SSO access token, etc.", example = "", required = true)
        @JsonProperty
        val credential: ProtectedProperty<String> = ProtectedProperty(""),

        @ApiModelProperty("A nickname, must be 2 to 16 letters.", example = "Good nickname", required = true)
        @JsonProperty
        val nickname: String = "",

        @ApiModelProperty("User gender. Empty value or omitting this field means your gender " +
                "will not be disclosed to other people.", example = "m", required = false)
        @JsonProperty
        val gender: Gender? = Gender.UNDEFINED,

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
        val appVersion: String = "",

        @ApiModelProperty("User's email address. Must be a valid email address or, just leave it blank or null.",
                example = "username@company.com", required = false)
        @JsonProperty
        @ValidEmail
        val email: String? = "",

        @ApiModelProperty("Invitation host's user id token, which is 16 digit alphanumeric literal.",
                example = "E91U5ORCURK7ZK5N", required = false)
        @JsonProperty
        val invitedBy: String? = ""
) {
    companion object : EmptyObject<CreateAccountRequestDto> {
        override val EMPTY = CreateAccountRequestDto()

        val VALIDATOR = object : NullsafeValidator<CreateAccountRequestDto> {
            override fun supports(klass: Class<*>) =
                    CreateAccountRequestDto::class.java.isAssignableFrom(klass)

            override fun validateNonNull(target: CreateAccountRequestDto, e: Errors): ValidationFailures? =
                    with(target) {
                        return@with when {
                            loginType == LoginType.BASIC && (username.isBlank() || credential.value.isBlank()) ->
                                ValidationFailures.VALUE_INSUFFICIENT
                            loginType == LoginType.BASIC && (
                                    requireNotNull(username).length < User.MINIMUM_NAME_LENGTH ||
                                            requireNotNull(username).length > User.MAXIMUM_NAME_LENGTH) ->
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
