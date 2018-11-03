/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.restapi.dto.account.AccessToken
import com.github.fj.restapi.persistence.entity.MyAuthentication
import com.github.fj.restapi.persistence.entity.User

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
interface AuthenticationBusiness {
    fun hash(data: ByteArray): ByteArray

    fun createAccessToken(user: User): MyAuthentication

    /**
     * @param token Base62 encoded access token.
     * @param iv Initialisation vector that is used to cipher given [token].
     */
    fun parseAccessToken(token: String, iv: ByteArray): AccessToken
}
