/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
@FunctionalInterface
interface ApplicationNameProvider {
    fun nameForPlatform(platform: PushPlatform): String
}
