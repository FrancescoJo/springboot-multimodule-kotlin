/*
 * boot-gs-multi-module skeleton.
 * Under no licences and warranty.
 */
package com.github.francescojo.appconfig

import org.apache.commons.dbcp2.BasicDataSource
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import javax.inject.Inject
import javax.sql.DataSource

/**
 * Database configuration used in 'application' module for boot-gs-multi-module
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@Configuration
open class DatabaseConfig @Inject constructor() : WebMvcConfigurerAdapter() {
    @Bean
    open fun dataSource(appContext: ApplicationContext): DataSource {
        return BasicDataSource().apply({
            appContext.environment.run({
                driverClassName = getProperty("spring.datasource.driver-class-name")
                url             = getProperty("spring.datasource.url")
                username        = getProperty("spring.datasource.username")
                password        = getProperty("spring.datasource.password")
            })
        })
    }

    @Suppress("SpringKotlinAutowiring")
    @Bean
    open fun databasePopulator(dataSource: DataSource, rl: ResourceLoader): DatabasePopulator {
        return ResourceDatabasePopulator().apply({
            loadResources(rl, "classpath:/sql/schemas/*.sql").forEach { addScript(it) }
            populate(dataSource.connection)
        })
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DatabaseConfig::class.java)
    }
}
