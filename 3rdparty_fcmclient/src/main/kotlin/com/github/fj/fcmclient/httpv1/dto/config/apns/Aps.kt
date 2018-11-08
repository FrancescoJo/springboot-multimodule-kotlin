/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config.apns

/**
 * APS Dictionary implementation.
 *
 * The aps dictionary contains the keys used by Apple to deliver
 * the notification to the user's device. The keys specify the
 * type of interactions that you want the system to use when
 * alerting the user.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#APS_Dictionary_Keys">APS Dictionary Keys</a>
 */
class Aps {
    private val _valueMap = HashMap<String, Any>()

    /**
     * Include this key when you want the system to display a standard alert or
     * a banner. The notification settings for your app on the user's device
     * determine whether an alert or banner is displayed.
     */
    var alert = Alert.EMPTY

    /**
     * Include this key when you want the system to modify the badge of your
     * app icon.
     *
     * If this key is negative, the badge is not changed.
     * To remove the badge, set the value of this key to 0.
     */
    var badge = 0

    /**
     * Include this key when you want the system to play a sound. The value of
     * this key is the name of a sound file in your app's main bundle or in the
     * `Library/Sounds` folder of your app's data container. If the sound file
     * cannot be found, or if you specify `default` for the value, the system
     * plays the default alert sound.
     *
     * For details about providing sound files for notifications;
     * see [Preparing Custom Alert Sounds](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/SupportingNotificationsinYourApp.html).
     */
    var sound = ""

    /**
     * Include this key with a value of 1 to configure a background update
     * notification. When this key is present, the system wakes up your app
     * in the background and delivers the notification to its app delegate.
     *
     * For information about configuring and handling background update
     * notifications, see [Configuring a Background Update notification](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html).
     */
    var contentAvailable = 0

    /**
     * Provide this key with a string value that represents the notification's
     * type. This value corresponds to the value in the identifier property of
     * one of your app's registered categories.
     *
     * To learn more about using custom actions, see
     * [Configuring Categories and Actionable Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/SupportingNotificationsinYourApp.html).
     */
    var category = ""

    /**
     * Provide this key with a string value that represents the app-specific
     * identifier for grouping notifications. If you provide a notification
     * Content app extension, you can use this value to group your
     * notifications together.
     *
     * For local notifications, this key corresponds to the [threadIdentifier](https://developer.apple.com/documentation/usernotifications/unnotificationcontent/1649860-threadidentifier)
     * property of the [UNNotificationContent](https://developer.apple.com/documentation/usernotifications/unnotificationcontent) object.
     */
    var threadId = ""

    fun toStruct(): Map<String, Any> {
        return _valueMap.apply {
            clear()
            val alertContents = alert.toStruct()
            alertContents.takeIf { it.isNotEmpty() }?.let { put("alert", it) }
            badge.takeIf { it >= 0 }?.let { put("badge", badge) }
            sound.takeIf { it.isNotEmpty() }?.let { put("sound", it) }
            contentAvailable.takeIf { it == 1 }?.let { put("content-available", it) }
            category.takeIf { it.isNotEmpty() }?.let { put("category", it) }
            threadId.takeIf { it.isNotEmpty() }?.let { put("thread-id", it) }
        }
    }

    companion object {
        val EMPTY = Aps()
    }
}
