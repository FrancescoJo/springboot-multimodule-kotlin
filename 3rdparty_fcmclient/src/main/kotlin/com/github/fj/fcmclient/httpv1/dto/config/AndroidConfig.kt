/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config

import com.github.fj.fcmclient.httpv1.dto.notification.AndroidNotification

/**
 * Android specific options for messages sent through [FCM connection server](https://goo.gl/4GLdUl).
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class AndroidConfig : Config {
    private val _valueMap = HashMap<String, Any>()

    /**
     * An identifier of a group of messages that can be collapsed,
     * so that only the last message gets sent when delivery can be resumed.
     * A maximum of 4 different collapse keys is allowed at any given time.
     */
    var collapseKey = ""

    /**
     * HttpV1Message priority. Can take "normal" and "high" values.
     * For more information, see [Setting the priority of a message](https://goo.gl/GjONJv).
     */
    var priority = Priority.NORMAL

    /**
     * How long (in seconds) the message should be kept in FCM storage
     * if the device is offline. The maximum time to live supported is
     * 4 weeks, and the default value is 4 weeks if not set. Set it to
     * 0 if want to send the message immediately. In JSON format, the
     * Duration type is encoded as a string rather than an object, where
     * the string ends in the suffix "s" (indicating seconds) and is
     * preceded by the number of seconds, with nanoseconds expressed as
     * fractional seconds. For example, 3 seconds with 0 nanoseconds
     * should be encoded in JSON format as "3s", while 3 seconds and
     * 1 nanosecond should be expressed in JSON format as "3.000000001s".
     * The ttl will be rounded down to the nearest second.
     *
     * A duration in seconds with up to nine fractional digits, terminated by 's'. Example: `"3.5s"`.
     */
    var ttl = ""

    /**
     * Package name of the application where the registration tokens
     * must match in order to receive the message.
     */
    var restrictedPackageName = ""

    /**
     * Arbitrary key/value payload. If present, it will override
     * [google.firebase.fcm.v1.HttpV1Message.data](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#HttpV1Message.FIELDS.data).
     *
     * An object containing a list of `"key": value` pairs. Example:
     * ```
     * { "name": "wrench", "mass": "1.3kg", "count": "3" }
     * ```
     */
    val data: MutableMap<String, String> = HashMap()

    /** Notification to send to android devices. */
    var notification : AndroidNotification? = null

    override val name = CONFIG_NAME

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                collapseKey.takeIf { it.isNotEmpty() }?.let { put("collapse_key", it) }
                put("priority", priority)
                ttl.takeIf { it.isNotEmpty() }?.let { put("ttl", it) }
                restrictedPackageName.takeIf { it.isNotEmpty() }?.let { put("restricted_package_name", it) }
                data.takeIf { it.isNotEmpty() }?.let { put("data", it) }
                notification?.valueMap?.takeIf { it.isNotEmpty() }?.let { put("notification", it) }
            }
        }

    companion object {
        private const val CONFIG_NAME = "android"
    }

    /** Priority of a message to send to Android devices. */
    enum class Priority {
        NORMAL,
        HIGH
    }
}
