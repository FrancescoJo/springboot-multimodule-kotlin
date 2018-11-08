/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Nov - 2018
 */
public class ApplicationContextHolderPopulator implements TestContextEventDispatcher.Handler {
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
        ApplicationContextHolder.getInstance().setApplicationContext(context);
    }

    @SuppressWarnings("ConstantConditions")
    private void onContextClosed() {
        ApplicationContextHolder.getInstance().setApplicationContext(null);
    }
}
