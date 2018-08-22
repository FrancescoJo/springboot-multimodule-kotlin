/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util

import com.github.fj.lib.text.matchesIn
import java.util.TreeSet
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.Collection
import kotlin.collections.MutableSet
import kotlin.collections.forEach

/**
 * A [java.util.Set] implementation which stores and searches a
 * regular expression. This class is designed for single word matching and
 * not for dynamic data storage. Removing an individual element in this set is
 * not supported - [clear] is the only option to initialise.
 *
 * Any given input which contains special characters for regex - mostly
 * non alphanumeric characters such as `!$^&*+-.:<=>?(){}[]^` treated as
 * regular expression.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Mar - 2018
 */
class RegExpWordSet: MutableSet<String> {
    private val wordSet  = TreeSet<String>()
    private val regExSet = ArrayList<Pattern>()

    override fun add(element: String): Boolean {
        if (element.containsRegexChar()) {
            regExSet.add(Pattern.compile(element))
        }
        return wordSet.add(element)
    }

    override fun addAll(elements: Collection<String>): Boolean {
        var result = false
        elements.forEach { result = add(it) }
        return result
    }

    override fun clear() {
        wordSet.clear()
        regExSet.clear()
    }

    override fun iterator() = wordSet.iterator()

    override fun remove(element: String): Boolean =
            throw UnsupportedOperationException()

    override fun removeAll(elements: Collection<String>) =
            throw UnsupportedOperationException()

    override fun retainAll(elements: Collection<String>): Boolean =
            throw UnsupportedOperationException()

    override val size: Int
        get() = wordSet.size

    override fun contains(element: String) = contains(element, false)

    fun contains(element: String, ignoreCase: Boolean = false): Boolean {
        if (wordSet.contains(element)) {
            return true
        }

        regExSet.forEach {
            if (it.matcher(element).matches()) {
                return true
            }
        }

        if (ignoreCase) {
            wordSet.forEach {
                if (it.equals(element, true)) {
                    return true
                }

                val pattern = Pattern.compile(it, Pattern.CASE_INSENSITIVE)
                if (element.matchesIn(pattern)) {
                    return true
                }
            }
        }

        return false
    }

    override fun containsAll(elements: Collection<String>) =
            containsAll(elements, false)

    fun containsAll(elements: Collection<String>, ignoreCase: Boolean = false)
            : Boolean {
        elements.forEach {
            if (contains(it, ignoreCase)) {
                return true
            }
        }
        return false
    }

    override fun isEmpty() = wordSet.isEmpty()

    private fun String.containsRegexChar(): Boolean {
        this.forEach {
            if (REGEX_SPECIAL_CHARS.contains(it)) {
                return true
            }
        }

        return false
    }

    companion object {
        private const val REGEX_SPECIAL_CHARS = "!\\$^&*+-.:<=>?(){}[]^|"
    }
}
