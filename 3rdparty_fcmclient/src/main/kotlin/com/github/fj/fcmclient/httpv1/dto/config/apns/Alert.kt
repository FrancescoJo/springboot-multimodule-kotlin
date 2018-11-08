/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config.apns

/**
 * Alert Dictionary implementation.
 *
 * Include this key when you want the system to display a standard alert or
 * a banner. The notification settings for your app on the user's device
 * determine whether an alert or banner is displayed.
 *
 * The preferred value for this key is a dictionary, the keys for which are
 * listed in [here](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#//apple_ref/doc/uid/TP40008194-CH17-SW5).
 * If you specify a string as the value of this key, that string is displayed
 * as the message text of the alert or banner.
 *
 * *NOTE:* The JSON `\U` notation is not supported. Put the actual UTF-8 character
 * in the alert text instead.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#Alert_Keys">Alert Keys</a>
 */
class Alert {
    private val _valueMap = HashMap<String, Any>()

    /**
     * A short string describing the purpose of the notification. Apple Watch
     * displays this string as part of the notification interface. This string
     * is displayed only briefly and should be crafted so that it can be
     * understood quickly. This key was added in iOS 8.2.
     */
    var title = ""

    /** The text of the alert message.*/
    var body = ""

    /**
     * The key to a title string in the `Localizable.strings` file for the
     * current localization. The key string can be formatted with `%@` and
     * `%n$@` specifiers to take the variables specified in the
     * [titleLocaleArgs].
     *
     * See [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html)
     * for more information. This key was added in iOS 8.2.
     */
    var titleLocaleKey = ""

    /**
     * Variable string values to appear in place of the format specifiers
     * in [titleLocaleKey].
     *
     * See [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html)
     * for more information. This key was added in iOS 8.2.
     */
    val titleLocaleArgs: MutableSet<String> = HashSet()

    /**
     * If a string is specified, the system displays an alert that includes
     * the Close and View buttons. The string is used as a key to get a
     * localized string in the current localization to use for the right
     * button's title instead of "View".
     *
     * See [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html)
     * for more information.
     */
    var actionLocaleKey = ""

    /**
     * A key to an alert-message string in a `Localizable.strings` file for
     * the current localization (which is set by the user’s language
     * preference). The key string can be formatted with `%@` and `%n$@`
     * specifiers to take the variables specified in the [localeArgs] array.
     *
     * See Localizing the Content of Your Remote Notifications for more information.
     */
    var localeKey = ""

    /**
     * Variable string values to appear in place of the format specifiers in
     * [localeKey].
     *
     * See [Localizing the Content of Your Remote Notifications](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html)
     * for more information.
     */
    val localeArgs: MutableSet<String> = HashSet()

    /**
     * The filename of an image file in the app bundle, with or without
     * the filename extension. The image is used as the launch image when users
     * tap the action button or move the action slider.
     *
     * If this property is not specified, the system either uses the previous
     * snapshot, uses the image identified by the `UILaunchImageFile` key in
     * the app's `Info.plist` file, or falls back to `Default.png`.
     */
    var launchImage = ""

    fun toStruct(): Map<String, Any> {
        return _valueMap.apply {
            clear()
            title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
            body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
            titleLocaleKey.takeIf { it.isNotEmpty() }?.let { put("title-loc-key", it) }
            titleLocaleArgs.takeIf { it.isNotEmpty() }?.let { put("title-loc-args", it) }
            actionLocaleKey.takeIf { it.isNotEmpty() }?.let { put("action-loc-key", it) }
            localeKey.takeIf { it.isNotEmpty() }?.let { put("loc-key", it) }
            localeArgs.takeIf { it.isNotEmpty() }?.let { put("loc-args", it) }
            launchImage.takeIf { it.isNotEmpty() }?.let { put("launch-image", it) }
        }
    }

    companion object {
        val EMPTY = Alert()
    }
}