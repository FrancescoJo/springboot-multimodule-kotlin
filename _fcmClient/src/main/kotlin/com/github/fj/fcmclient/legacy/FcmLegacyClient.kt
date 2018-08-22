/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy

import com.github.fj.fcmclient.FcmClientUtils
import com.github.fj.fcmclient.PushMessage
import com.github.fj.fcmclient.PushService
import com.github.fj.fcmclient.legacy.dto.DownstreamMessage
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.ExponentialBackOff
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 6 - Feb - 2018
 */
class FcmLegacyClient(private val serverKey: String): PushService {
    @Throws(IOException::class)
    override fun validatePushToken(applicationName: String, pushToken: String): Boolean =
            FcmClientUtils.validatePushToken(LOG, serverKey, applicationName, pushToken)

    @Throws(IOException::class)
    override fun sendPush(message: PushMessage) {
        if (message !is DownstreamMessage) {
            throw IllegalArgumentException("Only ${DownstreamMessage::javaClass} is allowed as " +
                                           "message content.")
        }

        val url      = "https://fcm.googleapis.com/fcm/send"
        val contents = message.toJsonString()

        val response = NetHttpTransport().createRequestFactory().buildPostRequest(
                GenericUrl(url),
                ByteArrayContent("application/json", contents.toByteArray(Charsets.UTF_8))
        ).apply {
            headers.authorization = "key=$serverKey"
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

        LOG.debug("FCM << {} {}", response.statusCode, response.statusMessage)
        response.headers.forEach { name, value -> LOG.debug("FCM << {}: {}", name, value) }
        val responseText = response.parseAsString()
        LOG.debug("FCM << {}", responseText)

        val result = Gson().fromJson(responseText, FcmLegacyResponse.JsonReceiver::class.java).materialise()
        if (result.failureCount > 0) {
            if (result.results.containsKey("error")) {
                val errorReason = result.results["error"]!!
                when (errorReason) {
                    "NotRegistered" -> {
                        // User may deleted or not installed our app.
                        throw PeerNotRegisteredException()
                    }
                }
            }

            throw DownstreamErrorResponseException(result)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FcmLegacyClient::class.java)
    }
}
