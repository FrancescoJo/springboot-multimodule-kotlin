/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.util

import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.HttpTransportUtils
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.account.AccountRequestUtils

/**
 * All methods of this class must be called after the Test context is fully loaded, or will fail.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
class AccountUtils {
    static AuthenticationResponseDto createRandomAccount(final IntegrationTestBase testContext) {
        final CreateAccountRequestDto request = AccountRequestUtils.newRandomCreateAccountRequest()
        final WebTestClient.ResponseSpec responseSpec = testContext.testClient()
                .post()
                .uri(ApiPaths.API_V1_ACCOUNT)
                .body(BodyInserters.fromObject(request))
                .exchange()

        return HttpTransportUtils.expect2xxBody(responseSpec, AuthenticationResponseDto.class)
    }
}
