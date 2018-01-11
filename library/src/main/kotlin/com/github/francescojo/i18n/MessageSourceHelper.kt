/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.i18n

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Safer [org.springframework.context.MessageSource] class operation helper class.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
class MessageSourceHelper(private val messageSource: MessageSource) {
    fun getMessage(key: String, vararg args: Any): String {
        return try {
            messageSource.getMessage(key, args, LocaleContextHolder.getLocale())
        } catch (e: NoSuchMessageException) {
            LOG.warn("No message mapping found.", e)
            key
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(MessageSourceHelper::class.java)
    }
}
