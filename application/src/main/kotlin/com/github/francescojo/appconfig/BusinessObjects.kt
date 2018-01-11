/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.appconfig

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import javax.inject.Inject

/**
 * BO bean definitions used in 'application' module for boot-gs-multi-module
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@Configuration
open class BusinessObjects @Inject constructor() : WebMvcConfigurerAdapter()
