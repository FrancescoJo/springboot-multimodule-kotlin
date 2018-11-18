/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.restapi.Application
import com.github.fj.restapi.component.auth.HttpAuthScheme
import com.github.fj.restapi.appconfig.mvc.security.internal.HttpServletRequestAuthorizationHeaderFilter
import com.github.fj.restapi.endpoint.AbstractResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.ExchangeStrategies
import spock.lang.Specification
import test.com.github.fj.restapi.appconfig.ApplicationContextHolder
import test.com.github.fj.restapi.appconfig.IntegrationTestConfigurations

import javax.annotation.Nonnull

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

/**
 * A base spock test template class to provide basic test setup.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = [Application.class,
        IntegrationTestConfigurations.class])
@AutoConfigureWebClient
abstract class IntegrationTestBase extends Specification {
    @Value("\${server.port}")
    private int portValue

    @Autowired
    private ObjectMapper objMapper

    WebTestClient testClient() {
        return testClient("")
    }

    WebTestClient testClient(final @Nonnull String accessToken) {
        return newBasicWebTestClient().with {
            if (!accessToken.isEmpty()) {
                defaultHeader(HttpServletRequestAuthorizationHeaderFilter.HEADER_AUTHORIZATION,
                        "${HttpAuthScheme.TOKEN.typeValue} ${accessToken}")
                defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                // filter(logTransport())
            }
            return build()
        }
    }

    private WebTestClient.Builder newBasicWebTestClient() {
        // There'll be groovyc error without this explicit local reference declaration
        final mapper = objMapper

        // https://github.com/spring-projects/spring-boot/issues/8630
        // Yet WebTestClient does not honour default application configuration [08 - Nov - 2018]
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:$portValue")
                .exchangeStrategies(ExchangeStrategies.builder().codecs({ configurer ->
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper))
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper))
                })
                .build())
    }

    static <T> Closure<T> extractResponseAs(final Class<T> resultType) {
        return { response ->
            assert (response instanceof AbstractResponseDto), response
            final body = (response as AbstractResponseDto).body

            final ObjectMapper mapper = ApplicationContextHolder.getBean(ObjectMapper.class)
            return mapper.convertValue(body, resultType) as T
        }
    }
}
