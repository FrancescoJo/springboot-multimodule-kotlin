/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.regex.Pattern
import java.util.stream.Stream

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
        val withInput: (String) -> Boolean = { it.isNullOrUnicodeBlank() }

        assertEquals(true, withInput(""))
        assertEquals(true, withInput("   "))
        assertEquals(false, withInput(" a "))
        assertEquals(false, withInput("\u3000 a \u3000"))
        assertEquals(true, withInput("\u3000\u3000"))
    }

    @ParameterizedTest
    @MethodSource("matchesInTestArgsProvider")
    fun `matchesIn test for various inputs`(str: String, pattern: Pattern, expected: Boolean) {
        assertEquals(expected, str.matchesIn(pattern))
    }

    @Test
    fun `indentToString test`() {
        // given:
        val target = IndentToStringNested1().apply {
            id = 1
            nested = IndentToStringNested2().apply {
                id = 2
                nested = IndentToStringNested3().apply {
                    text = "Indent 3"
                }
            }
        }

        // when:
        val actual = target.toString()

        // then:
        assertEquals("""
            |IndentToStringNested1(
            |  id=1,
            |  nested=IndentToStringNested2(
            |    id=2,
            |    nested=IndentToStringNested3(
            |      text=Indent 3
            |    )
            |  )
            |)""".trimMargin(), actual)
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun matchesInTestArgsProvider(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of("1234567890", Pattern.compile("""\d+"""), true),
                    Arguments.of("AMZamz", Pattern.compile("""[A-Z]{3}[a-z]{3}"""), true),
                    Arguments.of("1992Apd", Pattern.compile("""\d+$"""), false)
            )
        }
    }

    private class IndentToStringNested1 {
        var id: Int = 0
        var nested: IndentToStringNested2 = IndentToStringNested2()
        override fun toString(): String {
            return "IndentToStringNested1(\n" +
                    "  id=$id,\n" +
                    "  nested=${indentToString(nested)}" +
                    ")"
        }
    }

    private class IndentToStringNested2 {
        var id: Int = 0
        var nested: IndentToStringNested3 = IndentToStringNested3()
        override fun toString(): String {
            return "IndentToStringNested2(\n" +
                    "  id=$id,\n" +
                    "  nested=${indentToString(nested)}" +
                    ")"
        }
    }

    private class IndentToStringNested3 {
        var text: String = ""
        override fun toString(): String {
            return "IndentToStringNested3(\n" +
                    "  text=$text\n" +
                    ")"
        }
    }
}
