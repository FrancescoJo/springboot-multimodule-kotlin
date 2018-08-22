/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient

import com.github.fj.fcmclient.legacy.ValidateKeyResponse
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.ExponentialBackOff
import com.google.gson.Gson
import com.google.gson.JsonParseException
import org.slf4j.Logger
import java.net.SocketException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Aug - 2018
 */
internal object FcmClientUtils {
    private val gson = Gson()

    fun validatePushToken(log: Logger, serverKey: String,
                          applicationName: String, pushToken: String): Boolean {
        val url = "https://iid.googleapis.com/iid/info/$pushToken"

        val response = try {
            NetHttpTransport().createRequestFactory().buildGetRequest(GenericUrl(url)).apply {
                headers.authorization = "key=$serverKey"
                /*
                 * HttpBackOffUnsuccessfulResponseHandler is designed to work with only one HttpRequest at a time.
                 * As a result, you MUST create a new instance of HttpBackOffUnsuccessfulResponseHandler with a new
                 * instance of BackOff for each instance of HttpRequest.
                 */
                unsuccessfulResponseHandler = HttpBackOffUnsuccessfulResponseHandler(ExponentialBackOff())

                log.debug("FCM >> {}", url)
                headers.forEach { name, value -> log.debug("FCM >> {}: {}", name, value) }
            }.execute()
        } catch (e: HttpResponseException) {
            log.warn("Unexpected response from server", e)
            return false
        } catch (e: SocketException) {
            log.warn("Network is unreachable", e)
            return false
        }

        log.debug("FCM << {} {}", response.statusCode, response.statusMessage)
        response.headers.forEach { name, value -> log.debug("FCM << {}: {}", name, value) }
        val responseText = response.parseAsString()
        val responses = try {
            gson.fromJson<Map<String, Any>>(responseText, Map::class.java)
        } catch (ex: JsonParseException) {
            log.warn("Unable to parse response as Map: {}", responseText)
            return false
        }

        if (responses.containsKey("error")) {
            log.warn("Response contains an error: {}", responseText)
            return false
        }

        val filteredMap = responses.filterKeys { ValidateKeyResponse.FIELDS.contains(it) }
        val result = try {
            ValidateKeyResponse.createFrom(filteredMap)
        } catch (ex: IllegalArgumentException) {
            log.warn("Unable to construct response as ValidateKeyResponse: {}", filteredMap)
            return false
        }

        log.debug("Expected application name: {}, FCM registered application name: {}",
                applicationName, result.application)

        return result.application == applicationName
    }
}