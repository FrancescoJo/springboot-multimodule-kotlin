/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.appconfig

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@TestConfiguration
class IntegrationTestConfigurations {
    @Bean
    TestContextEventDispatcher listener() {
        return new TestContextEventDispatcher(
                new TestDatasourceCleaner()
        )
    }
}
