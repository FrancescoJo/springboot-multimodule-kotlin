/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
public class TestContextEventDispatcher {
    private final List<Handler> listeners = new ArrayList<>();

    TestContextEventDispatcher(Handler... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @EventListener
    public void handleEvent(final ApplicationEvent event) {
        for (final Handler listener : listeners) {
            if (listener.canHandle(event)) {
                listener.onEvent(event);
            }
        }
    }

    interface Handler {
        boolean canHandle(final ApplicationEvent event);

        void onEvent(final ApplicationEvent event);
    }
}
