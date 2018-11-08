/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config.apns

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CreatingtheNotificationPayload.html">Creating the Remote Notification Payload</a>
 */
class Payload {
    private val _valueMap = HashMap<String, Any>()

    /**
     * The aps dictionary contains the keys used by Apple to deliver
     * the notification to the user’s device. The keys specify the
     * type of interactions that you want the system to use when
     * alerting the user.
     */
    var aps = Aps.EMPTY

    /**
     * A dictionary for app-specific data.
     */
    val customData: MutableMap<String, String> = HashMap()

    fun toStruct(): Map<String, Any> {
        return _valueMap.apply {
            clear()
            put("aps", aps.toStruct())
            customData.forEach { key, value -> put(key, value) }
        }
    }

    companion object {
        val EMPTY = Payload()
    }
}