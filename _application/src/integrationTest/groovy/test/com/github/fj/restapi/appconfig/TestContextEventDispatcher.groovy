/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig

import org.springframework.context.ApplicationEvent
import org.springframework.context.event.EventListener

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class TestContextEventDispatcher {
    private final List<Handler> listeners = new ArrayList<>()

    TestContextEventDispatcher(Handler... listeners) {
        this.listeners.addAll(listeners)
    }

    @EventListener
    void handleEvent(final ApplicationEvent event) {
        listeners.forEach({
            if (it.canHandle(event)) {
                it.onEvent(event)
            }
        })
    }

    interface Handler {
        boolean canHandle(final ApplicationEvent event)

        void onEvent(final ApplicationEvent event)
    }
}
