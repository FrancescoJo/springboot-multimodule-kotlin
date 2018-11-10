/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1.dto.config

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 5 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#resource-message">Firebase HTTP v1 API - HttpV1Message</a>
 */
interface Config {
    val name: String

    val valueMap: Map<String, Any>
}
