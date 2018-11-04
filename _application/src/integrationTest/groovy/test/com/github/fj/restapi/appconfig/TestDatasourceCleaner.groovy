/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig

import org.springframework.beans.BeansException
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.core.io.Resource

import javax.sql.DataSource
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.util.stream.Collectors

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class TestDatasourceCleaner implements TestContextEventDispatcher.Handler {
    private Map<String, Connection> dbConnections = new HashMap<>()
    private Map<String, List<String>> cleanupScripts = new HashMap<>()

    @Override
    boolean canHandle(final ApplicationEvent event) {
        return event instanceof ApplicationReadyEvent || event instanceof ContextClosedEvent
    }

    @Override
    void onEvent(final ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            onApplicationReady(event.getApplicationContext())
        } else if (event instanceof ContextClosedEvent) {
            onContextClosed()
        }
    }

    private void onApplicationReady(final ApplicationContext context) {
        try {
            final beanNames = context.getBeanNamesForType(DataSource.class)
            for (final String name : beanNames) {
                final cleanupFile = Paths.get(context.getResource("classpath:/$name-cleanup.sql")
                        .getURI())
                final cleanupScript = Arrays.asList(new String(Files.readAllBytes(cleanupFile)).split(";"))
                        .stream()
                        .map({ it.trim() })
                        .filter({ !it.isEmpty() })
                        .collect(Collectors.toList())

                dbConnections.put(name, context.getBean(name, DataSource.class).connection)
                cleanupScripts.put(name, cleanupScript)
            }
        } catch (final BeansException ignore) {
        }
    }

    private void onContextClosed() {
        dbConnections.forEach({ name, connection ->
            cleanupScripts.get(name).forEach { line ->
                connection.createStatement().execute(line)
            }
        })
    }
}
