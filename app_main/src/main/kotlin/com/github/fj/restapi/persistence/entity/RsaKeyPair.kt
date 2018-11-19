/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
@Entity
@Table(name = "rsa_key_pairs")
class RsaKeyPair {
    /** UUID string */
    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    var id: String = ""

    /**
     * Setting to `false` means all information signed by this public key are must not be trusted.
     * Doing so will GUEST login users lose their credential and history - be careful!
     */
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_enabled")
    var isEnabled: Boolean = false

    /**
     * Do not use the private key for encryption! Only for signing!
     */
    @Column(name = "private_key", columnDefinition = "TEXT")
    var privateKey: String = ""

    /**
     * Use the public key for encryption and the private key for decryption!
     */
    @Column(name = "public_key", columnDefinition = "TEXT")
    var publicKey: String = ""

    /**
     * Any signing attempt after `issuedAt` + [com.github.fj.restapi.appconfig.AppProperties.accessTokenAliveSecs] seconds
     * must be refused. Still, any keys with this id, are still can be verified with private key.
     */
    @Column(name = "issued_at")
    var issuedAt: LocalDateTime = LOCAL_DATE_TIME_MIN
}
