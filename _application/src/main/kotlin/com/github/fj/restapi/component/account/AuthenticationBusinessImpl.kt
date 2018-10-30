/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.lib.annotation.AllOpen
import org.springframework.stereotype.Component
import java.lang.UnsupportedOperationException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2018
 */
@AllOpen
@Component
class AuthenticationBusinessImpl : AuthenticationBusiness {
    private val msgDigest: MessageDigest by lazy { findBestDigester() }

    override fun hash(data: ByteArray): ByteArray = msgDigest.digest(data)

    private fun findBestDigester(): MessageDigest {
        for (alg in MESSAGE_DIGEST_ALGS) {
            try {
                return MessageDigest.getInstance(alg)
            } catch (ignore: NoSuchAlgorithmException) {
            }
        }

        throw UnsupportedOperationException("None of $MESSAGE_DIGEST_ALGS algorithms are supported on this system.")
    }

    companion object {
        private val MESSAGE_DIGEST_ALGS = listOf("SHA-256", "SHA1", "MD5")
    }
}
