/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.fcmclient.legacy.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 6 - Feb - 2018
 * @see <a href="https://firebase.google.com/docs/cloud-messaging/concept-options#setting-the-priority-of-a-message">Setting the priority of a message</a>
 */
enum class Priority(val key: String) {
    NORMAL("normal"),
    HIGH("high"),
    UNDEFINED("")
}