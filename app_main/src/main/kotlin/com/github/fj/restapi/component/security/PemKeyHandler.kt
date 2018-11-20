/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.security

import com.github.fj.lib.annotation.VisibleForTesting
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringReader
import java.io.StringWriter
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
@VisibleForTesting
internal open class PemKeyHandler {
    fun toPemFormat(key: Key): String = StringWriter().apply {
        PemWriter(this).use { it.writeObject(PemObject(key.format, key.encoded)) }
    }.toString()

    fun fromPemFormat(pemKey: String): Key {
        return with(StringReader(pemKey)) {
            PemReader(this).use { it.readPemObject() }
        }.run {
            val keyFactory = KeyFactory.getInstance("RSA", "BC")
            return@run when (type) {
                FORMAT_PKCS8 -> keyFactory.generatePrivate(PKCS8EncodedKeySpec(content))
                FORMAT_X509 -> keyFactory.generatePublic(X509EncodedKeySpec(content))
                else -> throw UnsupportedOperationException("$type type of key generation is " +
                        "not supported.")
            }
        }
    }

    companion object {
        private const val FORMAT_PKCS8 = "PKCS#8"
        private const val FORMAT_X509 = "X.509"
    }
}
