/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.notification

/**
 * [Web notification](https://www.w3.org/TR/notifications/#notification) to send via webpush protocol.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class WebPushNotification: Notification {
    private val _valueMap = HashMap<String, Any>()

    /**
     * The notification's title. If present, it will override
     * [google.firebase.fcm.v1.Notification.title](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.title).
     */
    var title = ""

    /**
     * The notification's body text. If present, it will override
     * [google.firebase.fcm.v1.Notification.body](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification.FIELDS.body).
     */
    var body = ""

    /** The URL to use for the notification's icon. */
    var icon = ""

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
                icon.takeIf { it.isNotEmpty() }?.let { put("icon", it) }
            }
        }

    companion object {
        val EMPTY = WebPushNotification()
    }
}