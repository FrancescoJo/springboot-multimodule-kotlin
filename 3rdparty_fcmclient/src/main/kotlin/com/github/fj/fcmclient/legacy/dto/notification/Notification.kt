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
 * @see <a href="https://firebase.google.com/docs/cloud-messaging/http-server-ref">Firebase Legacy HTTP protocol</a>
 */
interface Notification {
    /**
     * This field is internally used. Do not attempt to modify any contents
     * inside, or fcmClient will not perform its operations correctly.
     */
    val valueMap: Map<String, Any>
}
