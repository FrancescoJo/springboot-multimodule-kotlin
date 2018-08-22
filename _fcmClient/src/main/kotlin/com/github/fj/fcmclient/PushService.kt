/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient

import java.io.IOException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Aug - 2018
 */
interface PushService {
    /**
     * Validates given `pushToken`.
     *
     * https://developers.google.com/instance-id/reference/server#get_information_about_app_instances
     */
    @Throws(IOException::class)
    fun validatePushToken(applicationName: String, pushToken: String): Boolean

    /**
     * Sends given `message` to to specified recipient.
     *
     * @see <a href="https://firebase.google.com/docs/cloud-messaging/send-message#send_messages_using_the_legacy_app_server_protocols">Send messages using the legacy app server protocols</a>
     */
    @Throws(IOException::class)
    fun sendPush(message: PushMessage)
}
