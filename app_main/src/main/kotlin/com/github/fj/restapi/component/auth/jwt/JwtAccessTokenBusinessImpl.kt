/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.jwt

import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.security.RsaKeyPairManager
import com.github.fj.restapi.persistence.entity.User
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import org.springframework.security.core.Authentication
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
internal class JwtAccessTokenBusinessImpl(
        private val appProperties: AppProperties,
        private val rsaKeyPairManager: RsaKeyPairManager
) : AccessTokenBusiness {
    override fun create(user: User, timestamp: LocalDateTime): String {
        val now = utcNow()
        val issuer: String
        val tokenLifespan: Long
        appProperties.run {
            issuer = jwtIssuer
            tokenLifespan = accessTokenAliveSecs.toLong()
        }

        val entry = rsaKeyPairManager.getLatest()

        val jwtObject = JwtObject(
                issuer = issuer,
                subject = user.role,
                audience = Collections.singleton(user.idToken),
                expiration = now.plusSeconds(tokenLifespan),
                notBefore = LOCAL_DATE_TIME_MIN,
                issuedAt = now
        )

        val jwsHeader = JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT)
                .keyID(entry.keyId).build()
        JWSObject(jwsHeader, Payload(JwtObject.toJsonString(jwtObject))).run {
            sign(entry.rsaSigner)
            return serialize()
        }
    }

    override fun validate(token: String): Authentication {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
