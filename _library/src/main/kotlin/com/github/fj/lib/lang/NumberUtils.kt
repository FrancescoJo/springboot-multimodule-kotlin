/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.lang

/*
 * These functions are for avoiding an unexpected
 * java.lang.NumberFormatException.
 *
 * See kotlin.text.StringNumberConversions.kt for reference.
 */

/**
 * Parses given float literals safely. A [Float.MIN_VALUE] will be returned
 * if given String is not in a form of float literal.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Dec - 2017
 */
fun String.safeToFloat() = toFloatOrDefault(Float.MIN_VALUE)

fun String.toFloatOrDefault(defaultValue: Float): Float {
    if (this.isEmpty()) {
        return defaultValue
    }

    return try {
        java.lang.Float.parseFloat(this)
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

/**
 * Parses given float literals safely. A [Int.MIN_VALUE] will be returned
 * if given String is not in a form of int literal.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Dec - 2017
 */
fun String.safeToInt() = toIntOrDefault(Int.MIN_VALUE)

fun String.toIntOrDefault(defaultValue: Int): Int {
    if (this.isEmpty()) {
        return defaultValue
    }

    return try {
        java.lang.Integer.parseInt(this)
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

/**
 * Parses given float literals safely. A [Long.MIN_VALUE] will be returned
 * if given String is not in a form of long literal.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Dec - 2017
 */
fun String.safeToLong() = toLongOrDefault(Long.MIN_VALUE)

fun String.toLongOrDefault(defaultValue: Long): Long {
    if (this.isEmpty()) {
        return defaultValue
    }

    return try {
        java.lang.Long.parseLong(this)
    } catch (e: NumberFormatException) {
        defaultValue
    }
}
