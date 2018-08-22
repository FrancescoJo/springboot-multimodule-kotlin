/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config.apns

import com.github.fj.fcmclient.httpv1.dto.config.Config

/**
 * [Apple Push Notification Service](https://goo.gl/MXRTPa) specific options.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class ApnsConfig: Config {
    private val _valueMap = HashMap<String, Any>()

    /**
     * HTTP request headers defined in Apple Push Notification Service.
     * Refer to [APNs request headers](https://goo.gl/C6Yhia) for
     * supported headers, e.g. "apns-priority": "10".
     *
     * An object containing a list of `"key": value` pairs. Example:
     * ```
     * { "name": "wrench", "mass": "1.3kg", "count": "3" }.
     * ```
     */
    val headers: MutableMap<String, String> = HashMap()

    /**
     * APNs payload as a JSON object, including both aps dictionary and
     * custom payload. See [Payload Key Reference](https://goo.gl/32Pl5W).
     *
     * If present, it overrides [google.firebase.fcm.v1.Notification.title](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.title)
     * and [google.firebase.fcm.v1.Notification.body](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.body).
     */
    var payload = Payload.EMPTY

    override val name = CONFIG_NAME

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                headers.takeIf { it.isNotEmpty() }?.let { put("headers", it) }
                val payloadContents = payload.toStruct()
                payloadContents.takeIf { it.isNotEmpty() }?.let { put("payload", it) }
            }
        }

    companion object {
        private const val CONFIG_NAME = "apns"
    }
}
