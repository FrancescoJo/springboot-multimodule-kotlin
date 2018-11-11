/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

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

    companion object {
        const val TOKEN_ALIVE_DURATION_SECS = 60 * 60 * 24
    }
}
