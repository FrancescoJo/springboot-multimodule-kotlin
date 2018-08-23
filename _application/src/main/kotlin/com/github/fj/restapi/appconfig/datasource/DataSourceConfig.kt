/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.datasource

import com.github.fj.lib.annotation.AllOpen
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.jdbc.datasource.init.DatabasePopulator
import java.io.IOException
import javax.sql.DataSource

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Mar - 2018
 */
@AllOpen
@Configuration
class DataSourceConfig {
    @Value("classpath:/schemas/**/*.sql")
    private lateinit var schemaFiles: String

    @Bean
    fun databasePopulator(dataSource: DataSource, rl: ResourceLoader): DatabasePopulator {
        return SqlPopulator().apply {
            loadResources(rl, schemaFiles)
                    .sortedWith(Comparator { r1, r2 -> r1.uri.compareTo(r2.uri) })
                    .forEach { addScript(it) }
            populate(dataSource.connection)
        }
    }

    @Throws(IOException::class)
    private fun loadResources(rl: ResourceLoader, resourcePath: String) =
            ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resourcePath).toList()
}
