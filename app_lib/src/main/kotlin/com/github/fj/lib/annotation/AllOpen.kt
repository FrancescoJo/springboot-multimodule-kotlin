/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.annotation

/**
 * Basically all classes and methods of Kotlin language are designed not to be
 * inheritable. However, as the nature of Spring framework and mocking for
 * tests, we must break this basic principal.
 *
 * Kotlin language team suggests [All-open compiler plugin](https://kotlinlang.org/docs/reference/compiler-plugins.html)
 * to mitigate this problem. It helps that annotated class and its default
 * member functions are *opened by default*. To activate this annotation,
 * you must add plugin config for your build environment. Read the official
 * manual for more instructions.
 *
 * Although there is [All-open for Spring support](https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support)
 * and we decided not to use it, for not forgetting this restriction. It
 * would be a smell if spotting a code which is inheriting a class that is
 * annotated as `AllOpen`.
 *
 * Read these documents for mitigation:
 *
 * - [Mocking Kotlin with Mockito](https://hadihariri.com/2016/10/04/Mocking-Kotlin-With-Mockito/)
 * - [Never say final: mocking Kotlin classes in unit tests](https://medium.com/@dpreussler/never-say-final-mocking-kotlin-classes-in-unit-tests-314d275b82b1)
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Mar - 2018
 */
annotation class AllOpen
