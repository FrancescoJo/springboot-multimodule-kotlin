/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase

import com.github.fj.restapi.endpoint.OkResponseDto
import com.github.fj.restapi.endpoint.hello.dto.HelloRequestDto
import com.github.fj.restapi.endpoint.hello.dto.HelloResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
class HelloControllerIT extends IntegrationTestBase {
    def "POST /hello echos user name"() {
        given:
        final name = "MY_NAME"
        final request = new HelloRequestDto(name)
        final responseSpec = testClient()
                .post()
                .uri("${ApiPaths.HELLO}")
                .body(BodyInserters.fromObject(request))

        when:
        final response = responseSpec.exchange().expectStatus().is2xxSuccessful()
                .expectBody(OkResponseDto).returnResult().responseBody.with extractResponseAs(HelloResponseDto)

        then:
        response.message == "POST Hello, MY_NAME"
    }
}
