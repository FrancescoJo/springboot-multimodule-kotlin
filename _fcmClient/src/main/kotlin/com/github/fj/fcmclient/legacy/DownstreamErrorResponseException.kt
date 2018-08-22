/*
 * FCMClient
 *
 * Copyright 2018 Francesco Jo
 * Use of this software is subject to licence terms.
 */
package com.github.fj.fcmclient.legacy

import java.io.IOException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 6 - Feb - 2018
 */
class DownstreamErrorResponseException(val response: FcmLegacyResponse) :
        IOException("FCM Request was successful but did not reached to its endpoint.")