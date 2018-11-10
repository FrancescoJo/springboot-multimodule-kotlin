/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.restapi.persistence.consts.UserActivity
import com.github.fj.restapi.persistence.converter.entity.ByteArrayInetAddressConverter
import com.github.fj.restapi.persistence.converter.entity.UserActivityConverter
import java.io.Serializable
import java.net.InetAddress
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@Entity
@Table(name = "access_log", uniqueConstraints = [
    UniqueConstraint(name = "UK_Access_Log_History", columnNames = arrayOf("timestamp", "user_id"))
])
class AccessLog : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var seq: Long = 0L

    var timestamp: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0L

    @Convert(converter = UserActivityConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var activity: UserActivity = UserActivity.UNDEFINED

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "ip_addr", nullable = true, columnDefinition = "VARBINARY(16)")
    var ipAddr: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Column(columnDefinition = "TEXT")
    var input: String = ""

    @Column(columnDefinition = "TEXT")
    var output: String = ""

    @Transient
    var inputs = ArrayList<Any>()
}
