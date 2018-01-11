/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.svc.controller

import com.github.francescojo.i18n.MessageSourceHelper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

/**
 * Sample RESTController for boot-gs-multi-module
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@RestController
class HelloController @Inject constructor(private val i18nHelper: MessageSourceHelper) {
    @GetMapping("/")
    fun index(): String {
        return i18nHelper.getMessage("hello", i18nHelper.getMessage("app.name"))
    }
}
