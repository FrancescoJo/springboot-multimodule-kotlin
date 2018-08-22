/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config

import com.github.fj.fcmclient.httpv1.dto.notification.WebPushNotification

/**
 * [Webpush protocol](https://tools.ietf.org/html/rfc8030) options.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class WebPushConfig : Config {
    private val _valueMap = HashMap<String, Any>()

    /**
     * HTTP headers defined in webpush protocol. Refer to [Webpush protocol](https://tools.ietf.org/html/rfc8030#section-5)
     * for supported headers, e.g. "TTL": "15".
     *
     * An object containing a list of `"key": value` pairs. Example:
     * ```
     * { "name": "wrench", "mass": "1.3kg", "count": "3" }.
     * ```
     */
    val headers: MutableMap<String, String> = HashMap()

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

    /**
     * A [web notification](https://www.w3.org/TR/notifications/#notification) to send.
     */
    var notification = WebPushNotification.EMPTY

    override val name = CONFIG_NAME

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                headers.takeIf { it.isNotEmpty() }?.let { put("headers", it) }
                data.takeIf { it.isNotEmpty() }?.let { put("headers", it) }
                val notiContent = notification.valueMap
                notiContent.takeIf { it.isNotEmpty() }?.let { put("notification", it) }
            }
        }

    companion object {
        private const val CONFIG_NAME = "webpush"
    }
}
