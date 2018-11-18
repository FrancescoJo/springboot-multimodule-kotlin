/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.AppProperties.Companion.TOKEN_ALIVE_DURATION_SECS
import com.github.fj.restapi.component.auth.TokenGenerationMethod
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import javax.xml.bind.DatatypeConverter

/**
 * Manages all configuration values defined in application.yml file. Although the Spring framework
 * allows value injection via [org.springframework.beans.factory.annotation.Value] annotation
 * on every bean classes, it is still good to centralise single responsibility (serve all
 * configuration values, in this case) into a single class.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
@AllOpen
@Component
@EnableConfigurationProperties
class AppPropertiesImpl(
        @Value("\${app.authentication.token-generation}")
        private val _tokenGenMethod: String
) : AppProperties {
    override lateinit var accessTokenAes256Key: ByteArray

    override var accessTokenAliveSecs: Int = TOKEN_ALIVE_DURATION_SECS

    override var tokenGenerationMethod: TokenGenerationMethod =
            TokenGenerationMethod.byMethod(_tokenGenMethod)

    @Value("\${app.authentication.access-token-aes256-key}")
    fun accessTokenAes256Key(keyStr: String) {
        if (tokenGenerationMethod != TokenGenerationMethod.INHOUSE) {
            return
        }

        val concatenatedKeyStr = if (keyStr.length == AES256_KEY_HEX_LENGTH) {
            keyStr
        } else {
            with(keyStr.split(" ")) {
                if (size != AES256_KEY_BYTES_LENGTH) {
                    throw UnsupportedOperationException("AES 256 Key length != 32! " +
                            "Read application.yml.sample and provide a 32 digit hex letters " +
                            "for encryption key.")
                }

                return@with joinToString(separator = "") { s -> s }
            }
        }

        try {
            this.accessTokenAes256Key = DatatypeConverter.parseHexBinary(concatenatedKeyStr)
        } catch (e: IllegalArgumentException) {
            throw UnsupportedOperationException(
                    "Your AES256 Key contains non-hex character(s).", e)
        }
    }

    @Value("\${app.authentication.access-token-alive-seconds}")
    fun accessTokenAliveSecs(durationSecs: Int) {
        if (tokenGenerationMethod != TokenGenerationMethod.INHOUSE) {
            return
        }

        if (durationSecs <= 0) {
            throw UnsupportedOperationException(
                    "Access token life time seconds must be larger than 0.")
        }

        this.accessTokenAliveSecs = durationSecs
    }

    companion object {
        private const val AES256_KEY_HEX_LENGTH = 64
        private const val AES256_KEY_BYTES_LENGTH = 32
    }
}
