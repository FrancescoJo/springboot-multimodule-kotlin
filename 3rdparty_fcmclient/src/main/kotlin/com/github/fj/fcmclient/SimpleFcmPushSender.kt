/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.fcmclient

import com.github.fj.fcmclient.httpv1.FcmHttpV1Client
import com.github.fj.fcmclient.httpv1.dto.HttpV1Message
import com.github.fj.fcmclient.httpv1.dto.config.apns.ApnsConfig
import com.github.fj.fcmclient.httpv1.dto.notification.BasicNotification
import com.github.fj.fcmclient.httpv1.dto.target.TokenTarget
import com.github.fj.fcmclient.legacy.FcmLegacyClient
import com.github.fj.fcmclient.legacy.ValidateKeyResponse
import com.github.fj.fcmclient.legacy.dto.DownstreamMessage
import com.github.fj.fcmclient.legacy.dto.notification.IosNotification
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.ExponentialBackOff
import com.google.gson.Gson
import com.google.gson.JsonParseException
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.SocketException
import java.nio.charset.Charset

/**
 * FcmClient simple usage example. This class is hard to test.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Aug - 2018
 */
class SimpleFcmPushSender(privateKeyLocation: String,
                          projectId: String,
                          private val applicationNameProvider: ApplicationNameProvider,
                          private val serverKey: String,
                          private var mode: Mode = Mode.HTTP_V1) {
    private val fcmCredential = try {
        File(privateKeyLocation).inputStream().asString()
    } catch (t: IOException) {
        LOG.warn("Error while accessing Firebase credential file. Switching to ${Mode.LEGACY}.")
        LOG.warn("Caused by: ({})", t.toString())
        mode = Mode.LEGACY
        ""
    }

    private val legacyClient = FcmLegacyClient(serverKey, LOG)
    private val httpV1Client = FcmHttpV1Client(projectId, fcmCredential, LOG)
    private val gson = Gson()

    fun validatePushToken(platform: PushPlatform, token: String): Boolean {
        val url = "https://iid.googleapis.com/iid/info/$token"

        val response = try {
            NetHttpTransport().createRequestFactory().buildGetRequest(GenericUrl(url)).apply {
                headers.authorization = "key=$serverKey"
                /*
                 * HttpBackOffUnsuccessfulResponseHandler is designed to work with only one HttpRequest at a time.
                 * As a result, you MUST create a new instance of HttpBackOffUnsuccessfulResponseHandler with a new
                 * instance of BackOff for each instance of HttpRequest.
                 */
                unsuccessfulResponseHandler = HttpBackOffUnsuccessfulResponseHandler(ExponentialBackOff())

                LOG.debug("FCM >> {}", url)
                headers.forEach { name, value -> LOG.debug("FCM >> {}: {}", name, value) }
            }.execute()
        } catch (e: HttpResponseException) {
            LOG.warn("Unexpected response from server", e)
            return false
        } catch (e: SocketException) {
            LOG.warn("Network is unreachable", e)
            return false
        }

        LOG.debug("FCM << {} {}", response.statusCode, response.statusMessage)
        response.headers.forEach { name, value -> LOG.debug("FCM << {}: {}", name, value) }
        val responseText = response.parseAsString()
        val responses = try {
            gson.fromJson<Map<String, Any>>(responseText, Map::class.java)
        } catch (ex: JsonParseException) {
            LOG.warn("Unable to parse response as Map: {}", responseText)
            return false
        }

        if (responses.containsKey("error")) {
            LOG.warn("Response contains an error: {}", responseText)
            return false
        }

        val filteredMap = responses.filterKeys { ValidateKeyResponse.FIELDS.contains(it) }
        val result = try {
            ValidateKeyResponse.createFrom(filteredMap)
        } catch (ex: IllegalArgumentException) {
            LOG.warn("Unable to construct response as ValidateKeyResponse: {}", filteredMap)
            return false
        }

        val applicationName = applicationNameProvider.nameForPlatform(platform)

        LOG.debug("Expected application name: {}, FCM registered application name: {}",
                applicationName, result.application)

        return result.application == applicationName
    }

    fun sendPush(pushContent: PushContent) {
        val pushMessage = when (mode) {
            Mode.LEGACY -> createLegacyPushMessage(pushContent)
            Mode.HTTP_V1 -> createHttpV1PushMessage(pushContent)
        }

        return when (mode) {
            Mode.LEGACY -> legacyClient.sendPush(pushMessage)
            Mode.HTTP_V1 -> httpV1Client.sendPush(pushMessage)
        }
    }

    private fun createLegacyPushMessage(pushContent: PushContent): PushMessage {
        val targetPlatform = pushContent.receiverPlatform

        return DownstreamMessage().apply {
            when (targetPlatform) {
                PushPlatform.IOS -> {
                    recipients.add(pushContent.receiverDeviceToken)
                    notification = IosNotification().apply {
                        title = pushContent.title
                        body = pushContent.text.toString()
                    }
                    // https://github.com/firebase/quickstart-ios/issues/246
                    isContentAvailable = targetPlatform == PushPlatform.IOS
                    // https://github.com/firebase/firebase-ios-sdk/issues/149
                    pushContent.customData.takeIf { it.isNotEmpty() }?.let { customData ->
                        customData.entries.forEach { data[it.key] = it.value }
                    }
                }
                /*
                 * In contrast to the case of IOS, we don't create `Notification` object for
                 * Android clients. Since Android handles those type of notifications as
                 * `Notification`, the push message is displayed automatically by FCM service and
                 * the client may not be noticed by system that a push message has been arrived.
                 *
                 * Therefore, we omit AndroidNotification to make this PushMessage is handled as
                 * `Data` message.
                 *
                 * Read [Firebase message concept](https://firebase.google.com/docs/cloud-messaging/concept-options)
                 * for more information.
                 */
                PushPlatform.ANDROID -> {
                    recipients.add(pushContent.receiverDeviceToken)

                    val payloadData = HashMap<String, Any>().apply {
                        putAll(pushContent.customData)
                    }

                    /*
                     * These key names must be known to client developers
                     * to handle the push message correctly.
                     */
                    with(data) {
                        // May diverge to support multiple business requirements
                        put(KEY_TYPE, "push")
                        put(KEY_DATA, HashMap<String, Any>().apply {
                            put(KEY_TITLE, pushContent.title)
                            put(KEY_BODY, pushContent.text.toString())
                            put(KEY_EXTRAS, payloadData)
                        })
                    }
                }
                else -> throw UnsupportedOperationException("Not supported: $targetPlatform")
            }
        }
    }

    private fun createHttpV1PushMessage(pushContent: PushContent): PushMessage {
        val targetPlatform = pushContent.receiverPlatform
        val receiver = TokenTarget(pushContent.receiverDeviceToken)

        return HttpV1Message(receiver).apply {
            when (targetPlatform) {
                PushPlatform.IOS -> {
                    config = ApnsConfig().apply {
                        headers["title"] = pushContent.title
                        headers["body"] = pushContent.text.toString()
                    }
                }
                PushPlatform.ANDROID -> {
                    notification = BasicNotification().apply {
                        title = pushContent.title
                        body = pushContent.text.toString()
                    }
                }
                else -> throw UnsupportedOperationException("Not supported: $targetPlatform")
            }
            data.putAll(pushContent.customData)
        }
    }

    private fun InputStream.asString(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.readText() }
    }

    enum class Mode {
        /** Operates with legacy client only. */
        LEGACY,
        /** Operates with HTTP V1 client only. */
        HTTP_V1,
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SimpleFcmPushSender::class.java)

        private const val KEY_DATA = "data"
        private const val KEY_TYPE = "type"
        private const val KEY_TITLE = "title"
        private const val KEY_BODY = "body"
        private const val KEY_EXTRAS = "extras"
    }
}
