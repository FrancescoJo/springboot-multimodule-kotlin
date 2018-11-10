/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.service

import com.github.fj.fcmclient.ApplicationNameProvider
import com.github.fj.fcmclient.PushPlatform
import com.github.fj.fcmclient.SimpleFcmPushSender
import com.github.fj.lib.annotation.AllOpen
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
class PushConfig {
    @Value("\${app.push.fcm-private-key-location}")
    private var fcmPrivateKeyLocation = ""

    @Value("\${app.push.fcm-server-key}")
    private var fcmServerKey = ""

    @Value("\${app.push.fcm-project-id}")
    private var fcmProjectId = ""

    @Value("\${app.push.fcm-android-app-name}")
    private var fcmAndroidAppName = ""

    @Value("\${app.push.fcm-ios-app-name}")
    private var fcmIosAppName = ""

    @Value("\${app.push.fcm-web-app-name}")
    private var fcmWebAppName = ""

    @Bean
    fun fcmPushSenderService() = SimpleFcmPushSender(fcmPrivateKeyLocation, fcmProjectId,
            object : ApplicationNameProvider {
                override fun nameForPlatform(platform: PushPlatform): String = when (platform) {
                    PushPlatform.ANDROID -> fcmAndroidAppName
                    PushPlatform.IOS -> fcmIosAppName
                    PushPlatform.WEB -> fcmWebAppName
                    else -> throw UnsupportedOperationException("Push to $platform is not supported.")
                }
            }, fcmServerKey)
}
