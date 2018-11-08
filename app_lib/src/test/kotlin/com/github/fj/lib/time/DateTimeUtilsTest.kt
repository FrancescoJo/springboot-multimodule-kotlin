/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
class DateTimeUtilsTest {
    @Test
    fun `timestamp conversions demo`() {
        // given:
        val now = utcNow().withNano(0)

        // when:
        val longTimestamp = now.utcEpochSecond()
        val intTimestamp = longTimestamp.toInt()
        val systemEpoch = System.currentTimeMillis()

        // then:
        assertEquals(utcLocalDateTimeOf(longTimestamp).withNano(0), now)
        assertEquals(utcLocalDateTimeOf(intTimestamp).withNano(0), now)
        assertEquals(utcLocalDateTimeOf(systemEpoch).withNano(0), now)
    }
}