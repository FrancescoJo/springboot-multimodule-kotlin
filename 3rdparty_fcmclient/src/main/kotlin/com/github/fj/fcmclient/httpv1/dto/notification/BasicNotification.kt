/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.notification

/**
 * Basic notification template to use across all platforms.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 */
class BasicNotification : Notification {
    private val _valueMap = HashMap<String, Any>()

    /** The notification's title. */
    var title = ""

    /** The notification's body text. */
    var body  = ""

    override val valueMap: Map<String, Any>
        get() {
            return _valueMap.apply {
                title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
            }
        }
}
