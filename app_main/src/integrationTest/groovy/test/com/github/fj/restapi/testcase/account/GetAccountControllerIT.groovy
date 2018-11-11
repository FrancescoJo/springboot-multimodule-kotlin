/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.OkResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.ProfileResponseDto
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

import java.time.temporal.ChronoUnit

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Nov - 2018
 */
class GetAccountControllerIT extends IntegrationTestBase {
    def "Can get member profile with genuine access token"() {
        given:
        final authentication = AccountUtils.createRandomAccount(this)

        when:
        final responseSpec = makeResponseSpec(authentication.accessToken.value)

        and:
        final response = responseSpec
                .expectStatus().is2xxSuccessful()
                .expectBody(OkResponseDto).returnResult().responseBody
                .with extractResponseAs(ProfileResponseDto)

        then:
        response.id == authentication.id
        response.nickname == authentication.nickname
        response.gender == authentication.gender
        response.lastActive >= authentication.lastActive.truncatedTo(ChronoUnit.SECONDS)
    }

    private WebTestClient.ResponseSpec makeResponseSpec(final String accessToken) {
        return testClient(accessToken)
                .method(HttpMethod.GET)
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
    }
}
