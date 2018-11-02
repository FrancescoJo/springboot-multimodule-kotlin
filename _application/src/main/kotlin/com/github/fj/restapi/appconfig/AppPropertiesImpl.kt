/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.github.fj.lib.annotation.AllOpen
import org.springframework.beans.factory.InitializingBean
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
class AppPropertiesImpl : AppProperties, InitializingBean {
    override lateinit var accessTokenAes256Key: ByteArray

    @Value("\${app.cipher.access-token-aes256-key}")
    private lateinit var _accessTokenAes256Key: String

    override fun afterPropertiesSet() {
        val joinedAes256Key = _accessTokenAes256Key.let {
            return@let if (it.length == 64) {
                it
            } else {
                with(it.split(" ")) {
                    if (size != 32) {
                        throw UnsupportedOperationException("AES 256 Key length != 32! Read application.yml.sample" +
                                " for sample configuration and provide a 32 digit hex letters for encryption key.")
                    }

                    return@with joinToString(separator = "") { s -> s }
                }
            }
        }

        try {
            this.accessTokenAes256Key = DatatypeConverter.parseHexBinary(joinedAes256Key)
        } catch (e: IllegalArgumentException) {
            throw UnsupportedOperationException("Your AES256 Key contains non-hex character(s).", e)
        }
    }
}
