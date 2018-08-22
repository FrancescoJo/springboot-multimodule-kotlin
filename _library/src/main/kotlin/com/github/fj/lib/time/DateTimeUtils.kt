/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.time

import java.time.LocalDateTime
import java.time.ZoneOffset

fun utcNow(): LocalDateTime {
    return LocalDateTime.now(ZoneOffset.UTC)
}

fun LocalDateTime.utcEpochSecond(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}
