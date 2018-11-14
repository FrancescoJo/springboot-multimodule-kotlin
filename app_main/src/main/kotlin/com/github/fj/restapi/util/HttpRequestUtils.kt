/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.util

import com.github.fj.lib.text.isNullOrUnicodeBlank
import java.net.InetAddress
import javax.servlet.http.HttpServletRequest

/**
 * This logic searches for "X-Real-IP" header first and will use the value if query was hit,
 * otherwise try on "X-forwarded-for" header as callback and use the value if found,
 * and will use [HttpServletRequest.getRemoteAddr] for last resort which is not accurate under
 * reverse proxy configuration.
 *
 * Note that this approach is not accurate on various web containers and additional logic may be
 * needed to fulfill your business requirement.
 */
fun HttpServletRequest.extractIpStr(): String {
    with(getHeader("X-Real-IP")) {
        if (!isNullOrUnicodeBlank() && !isUnknown()) {
            return this
        }
    }

    with(getHeader("Proxy-Client-IP")) {
        if (!isNullOrUnicodeBlank() && !isUnknown()) {
            return this
        }
    }

    with(getHeader("X-forwarded-for")) {
        if (isNullOrUnicodeBlank()) {
            return@with
        }

        split(",").let {
            if (!it[0].isUnknown()) {
                return it[0]
            }
        }
    }

    return remoteAddr
}

fun HttpServletRequest.extractInetAddress(): InetAddress =
        InetAddress.getByName(this.extractIpStr())

private fun String.isUnknown() = "unknown".equals(this, true)
