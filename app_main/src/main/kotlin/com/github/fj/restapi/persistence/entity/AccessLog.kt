/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.restapi.persistence.consts.UserActivity
import com.github.fj.restapi.persistence.converter.entity.UserActivityConverter
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@Entity
@Table(name = "access_log")
class AccessLog : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var seq: Long = 0L

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0L

    @Convert(converter = UserActivityConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var activity: UserActivity = UserActivity.UNDEFINED

    @Column(columnDefinition = "TEXT")
    var input: String = ""

    @Column(columnDefinition = "TEXT")
    var output: String = ""

    var timestamp: LocalDateTime = LOCAL_DATE_TIME_MIN
}
