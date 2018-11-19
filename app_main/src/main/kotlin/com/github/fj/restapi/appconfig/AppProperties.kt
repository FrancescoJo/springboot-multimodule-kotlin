/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.github.fj.restapi.component.auth.TokenGenerationMethod

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
interface AppProperties {
    val accessTokenAes256Key: ByteArray

    /**
     * Default: 24h
     */
    val accessTokenAliveSecs: Int

    val tokenGenerationMethod: TokenGenerationMethod

    val jwtIssuer: String

    companion object {
        /**
         * This value is for key spinning. Setting too small value will impact on performance -
         * a value more than at least 1 hour is preferable.
         */
        const val TOKEN_ALIVE_DURATION_SECS = 60 * 60 * 24
    }
}
