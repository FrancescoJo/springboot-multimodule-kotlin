/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.annotation

import kotlin.annotation.Target

/**
 * Annotates an enum type must have a constant named as 'UNDEFINED'.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2018
 */
@Target(AnnotationTarget.CLASS)
annotation class UndefinableEnum
