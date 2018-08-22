/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.service

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.service.push.FcmPushSenderServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@AllOpen
@Configuration
@EnableConfigurationProperties
class Push {
    @Value("\${app.push.fcm-private-key-location}")
    private var fcmPrivateKeyLocation = ""

    @Value("\${app.push.fcm-server-key}")
    private var fcmServerKey = ""

    @Value("\${app.push.fcm-project-id}")
    private var fcmProjectId = ""

    @Bean
    fun fcmPushSenderService() = FcmPushSenderServiceImpl(fcmPrivateKeyLocation,
            fcmServerKey, fcmProjectId)
}
