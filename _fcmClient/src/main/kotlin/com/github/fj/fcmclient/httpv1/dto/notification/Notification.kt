/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.notification

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#notification">Firebase HTTP v1 API - Notification</a>
 */
interface Notification {
    val valueMap: Map<String, Any>

    companion object {
        val EMPTY = object: Notification {
            override val valueMap by lazy { HashMap<String, Any>() }
        }
    }
}
