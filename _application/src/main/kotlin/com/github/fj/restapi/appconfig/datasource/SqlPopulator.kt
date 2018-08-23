/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.datasource

import com.github.fj.lib.io.asString
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DatabasePopulator
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLWarning

/**
 * There is [org.springframework.jdbc.datasource.init.ResourceDatabasePopulator] for standard
 * database population. However, its implementation cannot detect custom delimiters which is
 * supported by certain databases (ex. MySQL - DELIMITER keyword), therefore such scripts will
 * fail upon programme startup.
 *
 * This class considers \n\n as separation standard of multiple SQL statements in a single file.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Mar - 2018
 */
class SqlPopulator : DatabasePopulator {
    private val scripts = ArrayList<Resource>()

    fun addScript(resource: Resource) {
        scripts.add(resource)
    }

    override fun populate(connection: Connection) {
        for (script in scripts) {
            val stmts = script.inputStream.asString()
                    .split(BLOCK_SEPARATOR)
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

            for (sql in stmts) {
                connection.createStatement().use { stmt ->
                    try {
                        stmt.execute(sql)
                        var warn: SQLWarning? = stmt.warnings
                        while (warn != null) {
                            LOG.debug("SQLWarning ignored: state '{}', err code '{}', message [{}]",
                                    warn.sqlState, warn.errorCode, warn.message)
                            warn = warn.nextWarning
                        }
                    } catch (e: SQLException) {
                        LOG.error("Error while executing SQL in {}\n{}", script.uri, sql)
                        throw e
                    }
                }
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SqlPopulator::class.java)

        private const val BLOCK_SEPARATOR = "\n\n"
    }
}
