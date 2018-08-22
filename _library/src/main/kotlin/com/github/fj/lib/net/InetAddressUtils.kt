/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.net

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

/**
 * IPv4 to IPv6 rule is from
 * [RFC 4291](https://tools.ietf.org/html/rfc4291#section-2.5.5.2).
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2018
 */
fun InetAddress.toIpV6AddressBytes(): ByteArray {
    return when (this) {
        is Inet4Address -> byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0xFF.toByte(), 0xFF.toByte(),
                address[0], address[1], address[2], address[3]
        )
        is Inet6Address -> this.address
        else -> throw UnsupportedOperationException(
                "Not an IP compatible address: $hostAddress")
    }
}
