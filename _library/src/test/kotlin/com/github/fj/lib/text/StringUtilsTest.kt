/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
class StringUtilsTest {
    @Test
    fun `ynToBoolean yields expected result upon given input`() {
        val withInput: (String) -> Boolean = { it.ynToBoolean() }

        assertEquals(false, withInput("n"))
        assertEquals(false, withInput("N"))
        assertEquals(true , withInput("Y"))
        assertEquals(true , withInput("y"))
        assertEquals(false, withInput(""))
        assertEquals(true , withInput("aaaa"))
    }

    @Test
    fun `isUnicodeBlank yields expected result upon given input`() {
        val withInput: (String) -> Boolean = { it.isUnicodeBlank() }

        assertEquals(true, withInput(""))
        assertEquals(true, withInput("   "))
        assertEquals(false, withInput(" a "))
        assertEquals(false, withInput("\u3000 a \u3000"))
        assertEquals(true, withInput("\u3000\u3000"))
    }
}
