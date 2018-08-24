/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.fcmclient

import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
data class PushContent(val title: String,
                       val receiverDeviceToken: String,
                       val receiverPlatform: PushPlatform,
                       val text: CharSequence = "",
                       val customData: Map<String, Any> = Collections.emptyMap())
