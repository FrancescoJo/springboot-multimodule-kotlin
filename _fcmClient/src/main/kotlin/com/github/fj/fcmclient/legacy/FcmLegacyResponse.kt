/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy

import com.google.gson.annotations.SerializedName

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 6 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/cloud-messaging/http-server-ref#interpret-downstream">Interpreting a downstream message response</a>
 */
data class FcmLegacyResponse(val multicastId: Long, val successCount: Int, val failureCount: Int,
                             val canonicalIds: Int, val results: Map<String, Any>) {
    class JsonReceiver {
        @SerializedName("multicast_id")
        private var multicastId = 0L

        @SerializedName("success")
        private var successCount = 0

        @SerializedName("failure")
        private var failureCount = 0

        @SerializedName("canonical_ids")
        private var canonicalIds = 0

        @SerializedName("results")
        private var results: List<Map<String, Any>>? = null

        fun materialise() = FcmLegacyResponse(multicastId, successCount, failureCount, canonicalIds,
                HashMap<String, Any>().apply { results?.forEach { putAll(it) } })
    }
}
