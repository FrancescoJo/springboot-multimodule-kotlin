/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.service.push

import com.github.fj.fcmclient.PushMessage
import com.github.fj.fcmclient.PushPlatform.ANDROID
import com.github.fj.fcmclient.PushPlatform.IOS
import com.github.fj.fcmclient.httpv1.FcmHttpV1Client
import com.github.fj.fcmclient.legacy.FcmLegacyClient
import com.github.fj.fcmclient.legacy.dto.DownstreamMessage
import com.github.fj.fcmclient.legacy.dto.notification.IosNotification
import com.github.fj.lib.io.asString
import com.github.fj.service.push.FcmPushSenderServiceImpl.Mode.HTTP_V1
import com.github.fj.service.push.FcmPushSenderServiceImpl.Mode.LEGACY
import java.io.File

/**
 * FcmClient simple usage example.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Aug - 2018
 */
class FcmPushSenderServiceImpl(private val mode: Mode = HTTP_V1,
                               privateKeyLocation: String,
                               serverKey: String,
                               projectId: String) {
    private val legacyClient = FcmLegacyClient(serverKey)
    private val httpV1Client = FcmHttpV1Client(serverKey, projectId,
            File(privateKeyLocation).inputStream().asString())

    fun validatePushToken(applicationName: String, pushToken: String): Boolean {
        val pushService = when (mode) {
            LEGACY -> legacyClient
            HTTP_V1 -> httpV1Client
        }

        return pushService.validatePushToken(applicationName, pushToken)
    }

    fun sendPush(pushContent: PushContent) {
        val pushService = when (mode) {
            LEGACY -> legacyClient
            HTTP_V1 -> httpV1Client
        }

        val pushMessage = when (mode) {
            LEGACY -> createLegacyPushMessage(pushContent)
            HTTP_V1 -> TODO("Not implemented")
        }

        return pushService.sendPush(pushMessage)
    }

    private fun createLegacyPushMessage(pushContent: PushContent): PushMessage {
        val targetPlatform = pushContent.receiverPlatform

        return DownstreamMessage().apply {
            when (targetPlatform) {
                IOS -> {
                    recipients.add(pushContent.receiverDeviceToken)
                    notification = IosNotification().apply {
                        title = pushContent.senderId
                        body = pushContent.text.toString()
                    }
                    // https://github.com/firebase/quickstart-ios/issues/246
                    isContentAvailable = targetPlatform == IOS
                    // https://github.com/firebase/firebase-ios-sdk/issues/149
                    pushContent.customData.takeIf { it.isNotEmpty() }?.let { customData ->
                        customData.entries.forEach { data[it.key] = it.value }
                    }
                }
                /*
                 * In contrast to the case of IOS, we don't create `Notification` object for
                 * Android clients. Since Android handles those type of notifications as
                 * `Notification`, the push message is displayed automatically by FCM service and
                 * the client may not be noticed by system that a push message has been arrived.
                 *
                 * Therefore, we omit AndroidNotification to make this PushMessage is handled as
                 * `Data` message.
                 *
                 * Read [Firebase message concept](https://firebase.google.com/docs/cloud-messaging/concept-options)
                 * for more information.
                 */
                ANDROID -> {
                    recipients.add(pushContent.receiverDeviceToken)

                    val payloadData = HashMap<String, Any>().apply {
                        pushContent.customData.takeIf { it.isNotEmpty() }?.let { customData ->
                            customData.entries.forEach { put(it.key, it.value) }
                        }
                    }

                    /*
                     * These key names must be known to client developers
                     * to handle the push message correctly.
                     */
                    data.apply {
                        // May diverge to support multiple business requirements
                        put(KEY_TYPE, "push")
                        put(KEY_DATA, HashMap<String, Any>().apply {
                            put(KEY_TITLE, pushContent.senderId)
                            put(KEY_BODY, pushContent.text.toString())
                            put(KEY_EXTRAS, payloadData)
                        })
                    }
                }
                else -> throw UnsupportedOperationException("Not supported: $targetPlatform")
            }
        }
    }

    enum class Mode {
        /** Operates with legacy client only. */
        LEGACY,
        /** Operates with HTTP V1 client only. */
        HTTP_V1,
    }

    companion object {
        private const val KEY_DATA   = "data"
        private const val KEY_TYPE   = "type"
        private const val KEY_TITLE  = "title"
        private const val KEY_BODY   = "body"
        private const val KEY_EXTRAS = "extras"
    }
}
