/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.appconfig

import com.github.francescojo.i18n.MessageSourceHelper
import com.google.common.base.Charsets
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import java.util.Locale

/**
 * I18n message definitions used in 'application' module for boot-gs-multi-module
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@Configuration
open class TextI18n : WebMvcConfigurerAdapter() {
    @Bean
    open fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:translations/messages")
        messageSource.setDefaultEncoding(Charsets.UTF_8.toString())
        return messageSource
    }

    @Bean
    open fun localeResolver(): LocaleResolver {
        // Customise this to utilise peer configurations - cookie, req header, etc.
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.ENGLISH)
        return slr
    }

    @Bean
    open fun messageSourceHelper(messageSource: MessageSource): MessageSourceHelper {
        return MessageSourceHelper(messageSource)
    }
}
