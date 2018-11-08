/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2018
 */
class CollectionUtilsTest {
    @Test
    fun `resize yields correct result upon given data set`() {
        assertEquals(listOf(0)         , listOf(0, 1, 2).resize(1))
        assertEquals(listOf(0, 1)      , listOf(0, 1).resize(2))
        assertEquals(listOf(0, 1, 9, 9), listOf(0, 1).resize(4) { _ -> 9 })

        assertThrows<IllegalArgumentException> { listOf(0, 1).resize(4, null) }
    }

    @Test
    fun `sumLong yields correct result upon given data set`() {
        val values = listOf(
                SumLongTestSubject("", 1L),
                SumLongTestSubject("", 2L),
                SumLongTestSubject("", 3L)
        )
        val expected = 6L

        val actual = values.sumLong { it.points }
        assertEquals(expected, actual)
    }

    @Test
    fun `findInsertPosition yields correct result upon given sorted list`() {
        val values = listOf(1, 3, 4, 5, 6, 8, 9)
        val withPosition: (Int) -> Int = { values.findInsertPosition(it) }

        assertEquals(0, withPosition(0))
        assertEquals(1, withPosition(2))
        assertEquals(4, withPosition(5))
        assertEquals(7, withPosition(10))
    }

    private data class SumLongTestSubject(val name: String, val points: Long)
}
