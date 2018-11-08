/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.dto.account.DeleteAccountRequestDto
import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class DeleteAccountControllerIT extends IntegrationTestBase {
    // TODO: "Request must be rejected with unreliable access token"

    def "Account is deleted by genuine access Token"() {
        given:
        final accountInfo = AccountUtils.createRandomAccount(this)
        final request = new DeleteAccountRequestDto("Test deletion")

        when:
        final responseSpec = testClient(accountInfo.accessToken.value)
                .method(HttpMethod.DELETE)
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(request))
                .exchange()

        then:
        println(responseSpec)
    }
}
