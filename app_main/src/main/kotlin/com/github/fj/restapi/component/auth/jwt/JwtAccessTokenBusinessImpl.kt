/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.jwt

import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.auth.AccessTokenBusiness.Companion.LOG
import com.github.fj.restapi.component.auth.AuthenticationObjectImpl
import com.github.fj.restapi.component.security.RsaKeyPairManager
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.exception.GeneralHttpException
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import java.text.ParseException
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
internal class JwtAccessTokenBusinessImpl(
        private val appProperties: AppProperties,
        private val rsaKeyPairManager: RsaKeyPairManager,
        private val userRepo: UserRepository
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
                audience = user.idToken,
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
        val jwtObject = try {
            with(JWSObject.parse(token)) {
                rsaKeyPairManager.getById(header.keyID).let {
                    if (!verify(it.rsaVerifier)) {
                        logW("RSA verification failure")
                        throw AuthTokenException("Not a genuine jwt token.")
                    }
                }

                return@with JwtObject.fromJsonString(payload.toString())
            }
        } catch (e: ParseException) {
            logW("Wrong jwt format")
            throw AuthTokenException("Error while parsing token.", e)
        } catch (e: JOSEException) {
            LOG.error("Error in RSA key retrieval logic")
            throw GeneralHttpException.create(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to verify token.", e)
        }

        if (appProperties.jwtIssuer != jwtObject.issuer) {
            logW("Wrong issuer - ${jwtObject.issuer}")
            throw AuthTokenException("Not a genuine jwt token.")
        }

        val user = userRepo.findByIdToken(jwtObject.audience).takeIf { it.isPresent }
                ?.run { get() }
                ?: run {
                    logW("User not found")
                    throw AuthTokenException("Not a valid user.")
                }

        if (user.role != jwtObject.subject) {
            logW("Role mismatch - Expected: ${user.role} vs Actual: ${jwtObject.subject}")
            throw AuthTokenException("Not a genuine jwt token.")
        }

        if (jwtObject.issuedAt < jwtObject.notBefore) {
            logW("Wrong time constraint: ${jwtObject.issuedAt} < ${jwtObject.notBefore}")
            throw AuthTokenException("Not a genuine jwt token.")
        }

        if (utcNow() > jwtObject.expiration) {
            throw AuthTokenExpiredException("This token is expired.")
        }

        return AuthenticationObjectImpl(user, token)
    }

    private fun logW(message: String) {
        LOG.warn("Login attempt with tampered jwt token - $message")
    }
}
