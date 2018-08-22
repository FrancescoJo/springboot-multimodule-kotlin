/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.annotation

/**
 * Annotates this business logic requires database transaction for integrity.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 02 - Aug - 2018
 */
@Target(AnnotationTarget.FUNCTION)
annotation class TransactionRequired
