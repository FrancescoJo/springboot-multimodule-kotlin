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
class Member : Serializable {
    @Id
    var id: Long = 0L

    @Column(length = 63, nullable = false)
    var nickname: String = ""

    @Convert(converter = GenderConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var gender: Gender = Gender.UNDEFINED


    @Column(name = "last_active_timestamp", nullable = false)
    var lastActiveTimestamp: LocalDateTime = LocalDateTime.MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "last_active_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var lastActiveIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    companion object : EmptyObject<Member> {
        override val EMPTY = Member()
    }
}
