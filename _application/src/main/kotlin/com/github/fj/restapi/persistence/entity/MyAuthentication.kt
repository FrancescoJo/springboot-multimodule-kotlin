/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.util.EmptyObject
import com.github.fj.restapi.dto.account.AccessToken
import com.github.fj.restapi.persistence.converter.entity.AccessTokenEncodingConverter
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Stores AES 256 Encrypted user access token information.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2018
 */
@Entity
@Table(name = "users_authentication")
class MyAuthentication : Serializable {
    @Id
    var id: Long = 0L

    @Convert(converter = AccessTokenEncodingConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    @Enumerated(EnumType.STRING)
    var encoding: AccessToken.Encoded = AccessToken.Encoded.UNDEFINED

    @Column(length = 16, nullable = false, columnDefinition = "VARBINARY(16)")
    var iv: ByteArray = ByteArray(0)

    @Column(name = "access_token", length = 16, nullable = false, columnDefinition = "VARBINARY(254)")
    var rawAccessToken: ByteArray = ByteArray(0)

    @Column(name = "issued_date", nullable = true)
    var issuedDate: LocalDateTime? = null

    @Transient
    var accessToken: AccessToken? = null

    companion object : EmptyObject<MyAuthentication> {
        override val EMPTY = MyAuthentication()
    }
}
