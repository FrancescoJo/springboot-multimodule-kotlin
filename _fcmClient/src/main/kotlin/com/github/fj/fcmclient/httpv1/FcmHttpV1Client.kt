/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.httpv1

import com.github.fj.fcmclient.FcmClientUtils
import com.github.fj.fcmclient.PushMessage
import com.github.fj.fcmclient.PushService
import com.github.fj.fcmclient.httpv1.dto.HttpV1Message
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.ExponentialBackOff
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.IOException

/**
 * https://firebase.google.com/docs/cloud-messaging/send-message#send_messages_to_specific_devices
 * https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 2 - Feb - 2018
 */
class FcmHttpV1Client(private val serverKey: String, private val projectId: String,
                      private val credentials: String): PushService {
    /**
     * Acquires access token of Firebase services.
     *
     * @see <a href="https://firebase.google.com/docs/cloud-messaging/auth-server#authorize_http_v1_send_requests">Authorize HTTP v1 send requests</a>
     */
    private fun getAccessToken(): String {
        val googleCredential = GoogleCredential
                .fromStream(ByteArrayInputStream(credentials.toByteArray()))
                .createScoped(arrayListOf("https://www.googleapis.com/auth/firebase.messaging"))
        googleCredential.refreshToken()
        return googleCredential.accessToken
    }

    @Throws(IOException::class)
    override fun validatePushToken(applicationName: String, pushToken: String): Boolean =
            FcmClientUtils.validatePushToken(LOG, serverKey, applicationName, pushToken)

    @Throws(IOException::class)
    override fun sendPush(message: PushMessage) {
        if (message !is HttpV1Message) {
            throw IllegalArgumentException("Only ${HttpV1Message::javaClass} is allowed as " +
                                           "message content.")
        }

        val url = "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"
        val contents = message.toJsonString()

        val response = NetHttpTransport().createRequestFactory().buildPostRequest(
                GenericUrl(url),
                ByteArrayContent("application/json", contents.toByteArray(Charsets.UTF_8))
        ).apply {
            headers.authorization = "Bearer ${getAccessToken()}"
            /*
             * HttpBackOffUnsuccessfulResponseHandler is designed to work with only one HttpRequest at a time.
             * As a result, you MUST create a new instance of HttpBackOffUnsuccessfulResponseHandler with a new
             * instance of BackOff for each instance of HttpRequest.
             */
            unsuccessfulResponseHandler = HttpBackOffUnsuccessfulResponseHandler(ExponentialBackOff())

            LOG.debug("FCM >> {}", url)
            headers.forEach { name, value -> LOG.debug("FCM >> {}: {}", name, value) }
            LOG.debug("FCM >> {}", contents)
        }.execute()

        LOG.debug("FCM Response - {} {}", response.statusCode, response.statusMessage)
        response.headers.forEach { name, value -> LOG.debug("{}: {}", name, value) }
        LOG.debug(response.parseAsString())
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FcmHttpV1Client::class.java)
    }
}
