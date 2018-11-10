/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy.dto

import com.google.gson.Gson
import com.github.fj.fcmclient.PushMessage
import com.github.fj.fcmclient.legacy.dto.notification.Notification

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 6 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/cloud-messaging/http-server-ref#downstream-http-messages-json">Firebase Legacy Downstream HTTP message</a>
 */
class DownstreamMessage : PushMessage {
    /**
     * This parameter specifies the recipient(s) of a message.
     *
     * Unfortunately [DownstreamMessage] implementation does not support per-topic delivery.
     */
    val recipients: MutableSet<String> = HashSet()

    /**
     * This parameter identifies a group of messages (e.g., with
     * `collapse_key: "Updates Available"`) that can be collapsed, so that only
     * the last message gets sent when delivery can be resumed. This is
     * intended to avoid sending too many of the same messages when the
     * device comes back online or becomes active.
     *
     * Note that there is no guarantee of the order in which messages get sent.
     *
     * *Note*: A maximum of 4 different collapse keys is allowed at any given
     * time. This means a FCM connection server can simultaneously store 4
     * different messages per client app. If you exceed this number, there is
     * no guarantee which 4 collapse keys the FCM connection server will keep.
     */
    var collapseKey = ""

    /**
     * Sets the priority of the message. Valid values are [Priority.NORMAL] and
     * [Priority.HIGH]. On iOS, these correspond to APNs priorities `5` and `10`.
     *
     * By default, notification messages are sent with high priority, and data
     * messages are sent with normal priority. Normal priority optimizes the
     * client app's battery consumption and should be used unless immediate
     * delivery is required. For messages with normal priority, the app may
     * receive the message with unspecified delay.
     *
     * When a message is sent with high priority, it is sent immediately, and
     * the app can wake a sleeping device and open a network connection to
     * your server.
     *
     * For more information, see [Setting the priority of a message](https://firebase.google.com/docs/cloud-messaging/concept-options#setting-the-priority-of-a-message).
     */
    var priority = Priority.UNDEFINED

    /**
     * On iOS, use this field to represent content-available in the APNs
     * payload. When a notification or message is sent and this is set to true,
     * an inactive client app is awoken, and the message is sent through APNs
     * as a silent notification and not through the FCM connection server.
     * Note that silent notifications in APNs are not guaranteed to be
     * delivered, and can depend on factors such as the user turning on
     * Low Power Mode, force quitting the app, etc.
     *
     * On Android, data messages wake the app by default.
     *
     * On Chrome, currently not supported.
     */
    var isContentAvailable = false

    /**
     * Currently for iOS 10+ devices only. On iOS, use this field to
     * represent `mutable-content` in the APNs payload. When a notification
     * is sent and this is set to true, the content of the notification can be
     * modified before it is displayed, using a [Notification Service app](https://developer.apple.com/reference/usernotifications/unnotificationserviceextension)
     * extension. This parameter will be ignored for Android and web.
     */
    var isMutableContent = false

    /**
     * This parameter specifies how long (in seconds) the message should be
     * kept in FCM storage if the device is offline. The maximum time to live
     * supported is 4 weeks, and the default value is 4 weeks.
     * For more information, see [Setting the lifespan of a message](https://firebase.google.com/docs/cloud-messaging/concept-options#ttl).
     */
    var timeToLive = 0

    /**
     * Package name of the application where the registration tokens
     * must match in order to receive the message.
     */
    var restrictedPackageName = ""

    /**
     * This parameter, when set to `true`, allows developers to test a request
     * without actually sending a message.
     *
     * The default value is `false`.
     */
    var isDryRun = false

    /**
     * This parameter specifies the custom key-value pairs of the message's
     * payload.
     *
     * For example, with `"data": {"score":"3x1"}`
     *
     * On iOS, if the message is sent via APNs, it represents the custom data
     * fields. If it is sent via FCM connection server, it would be represented
     * as key value dictionary in `AppDelegate application:didReceiveRemoteNotification:`.
     *
     * On Android, this would result in an intent extra named `score` with the
     * string value `3x1`.
     *
     * The key should not be a reserved word (`from` or any word starting with
     * `google` or `gcm`). Do not use any of the words defined in this table
     * (such as `collapse_key`).
     *
     * Values in string types are recommended. You have to convert values in
     * objects or other non-string data types (e.g., integers or booleans)
     * to string.
     */
    val data: MutableMap<String, Any?> = HashMap()

    /**
     * This parameter specifies the predefined, user-visible key-value pairs
     * of the notification payload. See [Notification payload support](https://firebase.google.com/docs/cloud-messaging/http-server-ref#notification-payload-support)
     * for detail. For more information about notification message and data
     * message options, see [Message types](https://firebase.google.com/docs/cloud-messaging/concept-options#notifications_and_data_messages).
     * If a notification payload is provided, or the `content_available` option
     * is set to `true` for a message to an iOS device, the message is *sent
     * through APNs*, otherwise it is sent through the FCM connection server.
     */
    var notification: Notification? = null

    override fun toJsonString(): String = Gson().toJson(HashMap<String, Any>().apply {
        when (recipients.size) {
            1 -> Pair("to", recipients.first())
            else -> Pair("registration_ids", recipients)
        }.run { put(first, second) }
        collapseKey.takeIf { it.isNotEmpty() }?.let { put("collapse_key", it) }
        priority.takeIf { it != Priority.UNDEFINED }?.let { put("priority", it.key) }
        isContentAvailable.takeIf { it }?.let { put("content_available", it) }
        isMutableContent.takeIf { it }?.let { put("mutable_content", it) }
        timeToLive.takeIf { it > 0 }?.let { put("time_to_live", it) }
        restrictedPackageName.takeIf { it.isNotEmpty() }?.let { put("restricted_package_name", it) }
        isDryRun.takeIf { it }?.let { put("dry_run", it) }
        data.takeIf { it.isNotEmpty() }?.let { put("data", it) }
        notification?.valueMap?.takeIf { it.isNotEmpty() }?.let { put("notification", it) }
    })
}
