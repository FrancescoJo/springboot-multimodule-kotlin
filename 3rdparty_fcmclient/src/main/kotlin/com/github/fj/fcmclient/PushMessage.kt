/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Aug - 2018
 */
interface PushMessage {
    fun toJsonString(): String
}
