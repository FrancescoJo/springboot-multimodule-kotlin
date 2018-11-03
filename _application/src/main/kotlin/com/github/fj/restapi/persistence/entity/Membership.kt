/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.util.EmptyObject
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.converter.entity.ByteArrayInetAddressConverter
import com.github.fj.restapi.persistence.converter.entity.GenderConverter
import com.google.common.base.MoreObjects
import java.io.Serializable
import java.net.InetAddress
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Stores user information which is likely to be changed.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Entity
@Table(name = "members")
class Membership : Serializable {
    @Id
    var id: Long = 0L

    @Column(length = 63, nullable = false)
    var nickname: String = ""

    @Convert(converter = GenderConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    @Enumerated(EnumType.STRING)
    var gender: Gender = Gender.UNDEFINED

    @Column(name = "last_active_timestamp", nullable = true)
    var lastActiveTimestamp: LocalDateTime? = null

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "last_active_ip", nullable = true, columnDefinition = "VARBINARY(16)")
    var lastActiveIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Column(name = "suspended_on", nullable = true)
    var suspendedOn: LocalDateTime? = null

    @Column(name = "suspended_until", nullable = true)
    var suspendedUntil: LocalDateTime? = null

    override fun toString(): String {
        return "Member(" +
                "id=$id," +
                "nickname='$nickname'," +
                "gender=$gender," +
                "lastActiveTimestamp=$lastActiveTimestamp," +
                "lastActiveIp=$lastActiveIp," +
                "suspendedOn=$suspendedOn," +
                "suspendedUntil=$suspendedUntil" +
                ")"
    }

    companion object : EmptyObject<Membership> {
        override val EMPTY = Membership()
    }
}
