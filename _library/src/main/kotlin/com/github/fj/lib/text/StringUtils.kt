/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.regex.Pattern

/*
 * Referenced from: https://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
 * Pretty display at: https://www.fileformat.info/info/unicode/category/Zs/list.htm
 *
 * Type notation to ensure this collection is immutable even though a reference leakage happens
 */
private val UNICODE_BLANK_CHARS: Set<Char> = HashSet<Char>().apply {
    add('\u0009')   // control-0009
    add('\u000A')   // control-000A
    add('\u000B')   // control-000B
    add('\u000C')   // control-000C
    add('\u000D')   // control-000D
    add('\u001C')   // FILE SEPARATOR
    add('\u001D')   // GROUP SEPARATOR
    add('\u001E')   // RECORD SEPARATOR
    add('\u001F')   // UNIT SEPARATOR
    add('\u0020')   // SPACE
    add('\u0085')   // control-0085
    add('\u00A0')   // NO-BREAK SPACE
    add('\u1680')   // OGHAM SPACE MARK
    add('\u2000')   // EN QUAD
    add('\u2001')   // EM QUAD
    add('\u2002')   // EN SPACE
    add('\u2003')   // EM SPACE
    add('\u2004')   // THREE-PER-EM SPACE
    add('\u2005')   // FOUR-PER-EM SPACE
    add('\u2006')   // SIX-PER-EM SPACE
    add('\u2007')   // FIGURE SPACE
    add('\u2008')   // PUNCTUATION SPACE
    add('\u2009')   // THIN SPACE
    add('\u200A')   // HAIR SPACE
    add('\u202F')   // NARROW NO-BREAK SPACE
    add('\u205F')   // MEDIUM MATHEMATICAL SPACE
    add('\u3000')   // IDEOGRAPHIC SPACE
}

fun String?.isNullOrUnicodeBlank(): Boolean {
    if (isNullOrBlank()) {
        return true
    }

    this!!.forEach {
        if (!UNICODE_BLANK_CHARS.contains(it)) {
            return false
        }
    }

    return true
}

fun String.matchesIn(pattern: Pattern) = pattern.matcher(this).matches()

/**
 * Converts this String to boolean. This function will yield correct results
 * only on a String of one length. Expected results are described below:
 *
 * ------------------------------------------------------------------------
 *  Input String                                      | Result
 * ------------------------------------------------------------------------
 *  'N' or 'n'                                        | false
 *  'Y' or 'y'                                        | true
 *  Any string with > 2 letters                       | true
 *  Empty string                                      | false
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
fun String.ynToBoolean(): Boolean {
    return when (this.length) {
        0 -> false
        1 -> toLowerCase() != "n"
        else -> true
    }
}

fun Boolean.toYn(): String {
    return if (this) {
        "Y"
    } else {
        "N"
    }
}

/**
 * This function helps printing pretty indent in nested objects. It searches call stack of
 * calling point to determine indentation level for preserving pretty indentation level.
 * The indentation level will increase per [Any.toString] invocation(s).
 *
 * Due to its nature, consider not calling this function in your production codes.
 */
fun indentToString(target: Any?): String {
//    var level = 0
//    var entryPointFound = false
//    Thread.currentThread().stackTrace.forEach {
//        if (it.methodName == "indentToString") {
//            entryPointFound = true
//        }
//        if (entryPointFound && it.methodName == "toString") {
//            entryPointFound = false
//            level++
//        }
//    }

    val indents = "  "
    val str = target.toString()
    val split = str.split("\n")
    return if (split.size == 1) {
        indents + split
    } else {
        val sb = StringBuilder()

        sb.append(split[0]).append("\n")
        for (i in 1 until split.size) {
            sb.append(indents).append(split[i]).append("\n")
        }

        sb.toString()
    }
}

private const val RANDOM_ALPHANUMERIC_CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890"
private const val RANDOM_CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZz1234567890"

fun getRandomAlphaNumericString(length: Int) =
        getRandomAlphaNumericStringInternal(length, RANDOM_ALPHANUMERIC_CHARS)

fun getRandomCapitalAlphaNumericString(length: Int) =
        getRandomAlphaNumericStringInternal(length, RANDOM_CAPITAL_CHARS)

private fun getRandomAlphaNumericStringInternal(length: Int, pool: CharSequence): String {
    val random = ThreadLocalRandom.current()
    val sb = StringBuffer(length)
    for (loop in 0 until length) {
        val index = random.nextInt(pool.length)
        sb.append(pool[index])
    }

    return sb.toString()
}
