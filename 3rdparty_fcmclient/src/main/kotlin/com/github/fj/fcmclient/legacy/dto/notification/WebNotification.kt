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
class WebNotification : Notification {
    private val _valueMap = HashMap<String, Any>()

    /** The notification's title. */
    private var title = ""

    /** The notification's body text. */
    private var body = ""

    /** The URL to use for the notification's icon. */
    private var icon = ""

    /**
     * The action associated with a user click on the notification.
     *
     * For all URL values, secure HTTPS is required.
     */
    private var clickAction = ""

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                clear()
                title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
                icon.takeIf { it.isNotEmpty() }?.let { put("icon", it) }
                clickAction.takeIf { it.isNotEmpty() }?.let { put("click_action", it) }
            }
        }
}
