/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.security

import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
data class JwtRsaKeyPair(
        val keyId: String,

        /** Must be derived from public key */
        val rsaVerifier: RSASSAVerifier,

        /** Must be derived from private key */
        val rsaSigner: RSASSASigner,

        val expiredAt: LocalDateTime
)
