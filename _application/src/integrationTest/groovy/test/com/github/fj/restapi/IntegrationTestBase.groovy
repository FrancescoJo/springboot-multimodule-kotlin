/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.fj.restapi.Application
import com.github.fj.restapi.dto.AbstractResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

/**
 * A base spock test template class to provide basic test setup.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = Application.class)
@AutoConfigureWebClient
abstract class IntegrationTestBase extends Specification {
    @Value("\${server.port}")
    private int portValue

    protected WebTestClient testClient() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:$portValue")
                .build()
    }

    protected static <T> Closure<T> extractResponseAs(Class<T> resultType) {
        return { response ->
            assert (response instanceof AbstractResponseDto), response
            final body = (response as AbstractResponseDto).body

            final newMapper = new ObjectMapper().with {
                registerModule(new KotlinModule())
                // To tell Jackson ObjectMapper not to serialise/deserialse inherited getter/setter methods
                setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            }

            return newMapper.convertValue(body, resultType) as T
        }
    }
}
