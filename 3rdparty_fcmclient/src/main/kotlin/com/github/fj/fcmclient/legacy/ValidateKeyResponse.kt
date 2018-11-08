/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Feb - 2018
 */
internal data class ValidateKeyResponse(val applicationVersion: String, val application: String,
                                        val scope: String, val authorizedEntity: String, val platform: String) {
    companion object {
        private const val KEY_APPLICATION_VERSION = "applicationVersion"
        private const val KEY_APPLICATION         = "application"
        private const val KEY_SCOPE               = "scope"
        private const val KEY_AUTHORIZED_ENTITY   = "authorizedEntity"
        private const val KEY_PLATFORM            = "platform"

        val FIELDS = arrayOf(KEY_APPLICATION_VERSION, KEY_APPLICATION, KEY_SCOPE, KEY_AUTHORIZED_ENTITY, KEY_PLATFORM)

        fun createFrom(filteredMap: Map<String, Any>) =
                filteredMap.run {
                    ValidateKeyResponse(get(KEY_APPLICATION_VERSION).toString(),
                            get(KEY_APPLICATION).toString(),
                            get(KEY_SCOPE).toString(),
                            get(KEY_AUTHORIZED_ENTITY).toString(),
                            get(KEY_PLATFORM).toString()
                    )
                }
    }
}
