/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;

/**
 * Provides a static access of [ApplicationContext] instance of currently running test.
 * Use this class as least as possible, since `static` breaks advantages of Dependency Injection.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Nov - 2018
 */
public class ApplicationContextHolder /*implements ApplicationContextAware*/ {
    public static ApplicationContextHolder getInstance() {
        return LazyHolder.INSTANCE;
    }

    private ApplicationContext applicationContext = null;

    // @Override
    public void setApplicationContext(@Nonnull ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static class LazyHolder {
        static final ApplicationContextHolder INSTANCE = new ApplicationContextHolder();
    }
}
