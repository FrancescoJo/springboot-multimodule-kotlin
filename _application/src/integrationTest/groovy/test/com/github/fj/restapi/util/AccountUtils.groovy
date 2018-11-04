/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.util

import com.github.fj.restapi.dto.OkResponseDto
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.test.account.AccountRequestUtils
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase

/**
 * All methods of this class must be called after the Test context is fully loaded, or will fail.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
class AccountUtils {
    static AuthenticationResponseDto createRandomAccount(IntegrationTestBase testContext) {
        final request = AccountRequestUtils.newRandomCreateAccountRequest()
        final responseSpec = testContext.testClient()
                .post()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .body(BodyInserters.fromObject(request))

        final result = responseSpec.exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OkResponseDto).returnResult().responseBody
                .with IntegrationTestBase.extractResponseAs(AuthenticationResponseDto)

        return result
    }
}
