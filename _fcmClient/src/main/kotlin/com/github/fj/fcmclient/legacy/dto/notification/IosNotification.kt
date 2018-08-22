/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy.dto.notification

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/cloud-messaging/http-server-ref#notification-payload-support">Notification payload support</a>
 */
class IosNotification: Notification {
    private val _valueMap = HashMap<String, Any>()

    /**
     * The notification's title.
     *
     * This field is not visible on iOS phones and tablets.
     */
    var title = ""

    /** The notification's body text. */
    var body = ""

    /**
     * The sound to play when the device receives the notification.
     *
     * Sound files can be in the main bundle of the client app or in the
     * `Library/Sounds` folder of the app's data container. See the [iOS Developer Library](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/SupportingNotificationsinYourApp.html#//apple_ref/doc/uid/TP40008194-CH4-SW10)
     * for more information.
     */
    var sound = ""

    /**
     * The value of the badge on the home screen app icon.
     *
     * If negative, the badge is not changed.
     *
     * If set to 0, the badge is removed.
     */
    var badge = 0

    /**
     * The action associated with a user click on the notification.
     *
     * Corresponds to `category` in the APNs payload.
     *
     * See [Supporting Notifications in your App](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/SupportingNotificationsinYourApp.html) for more information.
     */
    var clickAction = ""

    /** The notification's subtitle. */
    var subtitle = ""

    /**
     * The key to the body string in the app's string resources to use to localize the body text to the user's current localization.
     *
     * Corresponds to `loc-key in` the APNs payload.
     *
     * See [Payload Key Reference](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html)
     * and [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html#//apple_ref/doc/uid/TP40008194-CH10-SW9)
     * for more information.
     */
    var bodyLocaleKey = ""

    /**
     * Variable string values to be used in place of the format specifiers in [bodyLocaleKey] to use to localize the body text to the user's current localization.
     *
     * Corresponds to `loc-args` in the APNs payload.
     *
     * See [Payload Key Reference](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html)
     * and [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html#//apple_ref/doc/uid/TP40008194-CH10-SW9)
     * for more information.
     */
    val bodyLocaleArgs: MutableSet<String> = HashSet()

    /**
     * The key to the title string in the app's string resources to use to localize the title text to the user's current localization.
     *
     * Corresponds to title-loc-key in the APNs payload.
     *
     * See [Payload Key Reference](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html)
     * and [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html#//apple_ref/doc/uid/TP40008194-CH10-SW9)
     * for more information.
     */
    var titleLocaleKey = ""

    /**
     * Variable string values to be used in place of the format specifiers in [titleLocaleKey] to use to localize the title text to the user's current localization.
     *
     * Corresponds to title-loc-args in the APNs payload.
     *
     * See [Payload Key Reference](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html)
     * and [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html#//apple_ref/doc/uid/TP40008194-CH10-SW9)
     * for more information.
     */
    val titleLocaleArgs: MutableSet<String> = HashSet()

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
                sound.takeIf { it.isNotEmpty() }?.let { put("sound", it) }
                badge.takeIf { it >= 0 }?.let { put("badge", badge) }
                clickAction.takeIf { it.isNotEmpty() }?.let { put("click_action", it) }
                subtitle.takeIf { it.isNotEmpty() }?.let { put("subtitle", it) }
                bodyLocaleKey.takeIf { it.isNotEmpty() }?.let { put("body_loc_key", it) }
                bodyLocaleArgs.takeIf { it.isNotEmpty() }?.let { put("body_loc_args", it) }
                titleLocaleKey.takeIf { it.isNotEmpty() }?.let { put("title_loc_key", it) }
                titleLocaleArgs.takeIf { it.isNotEmpty() }?.let { put("title_loc_args", it) }
            }
        }
}
