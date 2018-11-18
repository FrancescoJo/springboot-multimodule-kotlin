/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.jwt

import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2018
 */
data class JwtObject(
        /* issuer */

        /* subject */

        /* audience */
        val uidToken: String,

        /* expiration */

        /* not before */
        /* issued at */
        val issuedTimestamp: LocalDateTime

        /* jwt ID: the simplest: UUID.randomUUID().toString(); */
) {
    /**
     * These registered JWT claim names implementation follows RFC-7519. See
     * [JSON Web Token(JWT)](https://tools.ietf.org/html/rfc7519#section-4.2)
     * for more info.
     */
    companion object {
        /**
         * The principal that issued the JWT. The processing of this claim is
         * generally application specific. The "iss" value is a case-sensitive
         * string containing a StringOrURI value. Use of this claim is OPTIONAL.
         */
        private const val ISSUER = "iss"

        /**
         * The principal that is the subject of the JWT. The claims in a JWT
         * are normally statements about the subject. The subject value MUST
         * either be scoped to be locally unique in the context of the issuer
         * or be globally unique. The processing of this claim is generally
         * application specific. The "sub" value is a case-sensitive string
         * containing a StringOrURI value. Use of this claim is OPTIONAL.
         */
        private const val SUBJECT = "sub"

        /**
         * Identifies the recipients that the JWT is intended for. Each
         * principal intended to process the JWT MUST identify itself
         * with a value in the audience claim. If the principal processing
         * the claim does not identify itself with a value in the "aud" claim
         * when this claim is present, then the JWT MUST be rejected. In the
         * general case, the "aud" value is an array of case-sensitive strings,
         * each containing a StringOrURI value. In the special case when the
         * JWT has one audience, the "aud" value MAY be a single case-sensitive
         * string containing a StringOrURI value. The interpretation of
         * audience values is generally application specific.
         * Use of this claim is OPTIONAL.
         */
        private const val AUDIENCE = "aud"

        /**
         * identifies the expiration time on or after which the JWT MUST NOT
         * be accepted for processing. The processing of the "exp" claim
         * requires that the current date/time MUST be before the expiration
         * date/time listed in the "exp" claim. Implementers MAY provide for
         * some small leeway, usually no more than a few minutes, to account
         * for clock skew. Its value MUST be a number containing a NumericDate
         * value. Use of this claim is OPTIONAL.
         */
        private const val EXPIRATION = "exp"

        /**
         * Identifies the time before which the JWT MUST NOT be accepted for
         * processing. The processing of the "nbf" claim requires that the
         * current date/time MUST be after or equal to the not-before date/time
         * listed in the "nbf" claim. Implementers MAY provide for some small
         * leeway, usually no more than a few minutes, to account for clock
         * skew. Its value MUST be a number containing a NumericDate value.
         * Use of this claim is OPTIONAL.
         */
        private const val NOT_BEFORE = "nbf"

        /**
         * Identifies the time at which the JWT was issued. This claim can be
         * used to determine the age of the JWT. Its value MUST be a number
         * containing a NumericDate value. Use of thi claim is OPTIONAL.
         */
        private const val ISSUED_AT = "iat"

        /**
         * provides a unique identifier for the JWT. The identifier value MUST
         * be assigned in a manner that ensures that there is a negligible
         * probability that the same value will be accidentally assigned to a
         * different data object; if the application uses multiple issuers,
         * collisions MUST be prevented among values produced by different
         * issuers as well. The "jti" claim can be used to prevent the JWT from
         * being replayed. The "jti" value is a case-sensitive string.
         * Use of this claim is OPTIONAL.
         */
        private const val JWT_ID = "jti"
    }
}
