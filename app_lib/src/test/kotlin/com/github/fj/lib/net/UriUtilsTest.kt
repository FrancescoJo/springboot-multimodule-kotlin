/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.net

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2018
 */
class UriUtilsTest {
    @Test
    fun `pathSegments yields correct value upon given data`() {
        val withUri: (String) -> Int = { uri -> URI.create(uri).pathSegments().size }

        assertEquals(1, withUri("path"))
        assertEquals(2, withUri("path/id"))
        assertEquals(1, withUri("/"))
        assertEquals(2, withUri("/base_path/id"))
        assertEquals(3, withUri("/base_path//id"))
    }
}
