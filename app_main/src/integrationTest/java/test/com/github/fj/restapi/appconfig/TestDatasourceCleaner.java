/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.fj.lib.lang.ExceptionThrowingConsumer.exceptional;
import static com.github.fj.lib.lang.ExceptionThrowingConsumer.sneakyThrow;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
public class TestDatasourceCleaner implements TestContextEventDispatcher.Handler {
    private Map<String, Connection> dbConnections = new HashMap<>();
    private Map<String, List<String>> cleanupScripts = new HashMap<>();

    @Override
    public boolean canHandle(final ApplicationEvent event) {
        return event instanceof ApplicationReadyEvent || event instanceof ContextClosedEvent;
    }

    @Override
    public void onEvent(final ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            onApplicationReady(((ApplicationReadyEvent) event).getApplicationContext());
        } else if (event instanceof ContextClosedEvent) {
            onContextClosed();
        }
    }

    private void onApplicationReady(final ApplicationContext context) {
        try {
            final String[] beanNames = context.getBeanNamesForType(DataSource.class);
            for (final String name : beanNames) {
                final Path cleanupFile =
                        Paths.get(context.getResource("classpath:/" + name + "-cleanup.sql").getURI());
                final List<String> scriptLines = Arrays.stream(
                        new String(Files.readAllBytes(cleanupFile)).split(";"))
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());

                dbConnections.put(name, context.getBean(name, DataSource.class).getConnection());
                cleanupScripts.put(name, scriptLines);
            }
        } catch (final Exception e) {
            sneakyThrow(e);
        }
    }

    private void onContextClosed() {
        dbConnections.forEach((name, connection) -> cleanupScripts.get(name)
                .forEach(exceptional(line -> connection.createStatement().execute(line))));
    }
}
