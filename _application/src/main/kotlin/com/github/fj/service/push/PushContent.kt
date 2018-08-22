/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.service.push

import com.github.fj.fcmclient.PushPlatform
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
class PushContent(val senderId: String,
                  val receiverDeviceToken: String,
                  val receiverPlatform: PushPlatform,
                  val text: CharSequence = "",
                  val customData: Map<String, Any> = Collections.emptyMap())
