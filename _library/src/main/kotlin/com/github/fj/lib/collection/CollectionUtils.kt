/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.collection

import java.util.*

/**
 * Creates a new list that has size of `desiredSize`. Contents of newly created
 * list are based from given list. It is not restricted though, 0 `desiredSize`
 * invocation is discouraged.
 *
 * This function will:
 * - Shrinks given list if the size of it is larger than `desiredSize` and
 *   `filler` is ignored.
 * - Does nothing if the size of given list and `desiredSize` are equals
 * - Expands given list if the size of it is smaller than `desiredSize` and
 *   the rest will be filled by `filler`.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2018
 */
fun <T> List<T>.resize(desiredSize: Int, filler: ((Int) -> T)? = null): List<T> {
    return when {
        size > desiredSize -> {
            val newList = ArrayList<T>(desiredSize)
            for (i in 0 until desiredSize) {
                newList.add(this[i])
            }
            newList
        }
        size < desiredSize -> {
            if (filler == null) {
                throw IllegalArgumentException("Filler function must not be null when expanding a list!!")
            }

            val newList = ArrayList<T>(desiredSize)
            for (i in 0 until size) {
                newList.add(this[i])
            }
            for (i in size until desiredSize) {
                newList.add(filler.invoke(i))
            }
            newList

        }
        else -> this
    }
}

/**
 * Similar to [sumBy] function over Iterable<T>,
 * but returning type is [Long], not [Int].
 *
 * Returns the sum of all values produced by [selector] function applied to
 * each element in the collection.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2018
 */
fun <T> Iterable<T>.sumLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Collection<*>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}

/**
 * Finds the proper insertion position of given [input]
 * in a fully sorted [List].
 */
fun <T : Comparable<T>> List<T>.findInsertPosition(input: T): Int {
    var low = 0
    var hi  = this.size
    while (low < hi) {
        val diff = hi - low
        val mid = (hi + low) / 2
        val data = this[mid]

        if (diff == 1) {
            if (input > data) {
                low += 1
            }
            break
        } else {
            if (input > data) {
                low = mid
            } else if (input < data) {
                hi = mid
            } else {
                low = mid + 1
                break
            }
        }
    }

    return low
}
