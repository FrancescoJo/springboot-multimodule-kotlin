/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.collection

import java.util.*

fun getRandomBytes(length: Int): ByteArray = ByteArray(length).apply {
    Random().nextBytes(this)
}
