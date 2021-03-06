/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.util

import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.CreateAccountRequestDto
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.repository.AccessLogRepository
import com.github.fj.restapi.persistence.repository.MembershipRepository
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.lib.RandomHelper
import test.com.github.fj.restapi.HttpTransportUtils
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.account.AccountRequestUtils
import test.com.github.fj.restapi.appconfig.ApplicationContextHolder

/**
 * All methods of this class must be called after the Test context is fully loaded, or will fail.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
class AccountUtils {
    static AuthenticationResponseDto createRandomAccount(final IntegrationTestBase testContext) {
        return createRandomAccount(testContext, RandomHelper.randomEnumConst(LoginType.class))
    }

    static AuthenticationResponseDto createRandomAccount(final IntegrationTestBase testContext,
                                                         final LoginType loginType) {
        final CreateAccountRequestDto request = AccountRequestUtils.newRandomCreateAccountRequest(loginType)
        final WebTestClient.ResponseSpec responseSpec = testContext.testClient()
                .post()
                .uri(ApiPaths.API_V1_ACCOUNT)
                .body(BodyInserters.fromObject(request))
                .exchange()

        return HttpTransportUtils.expect2xxBody(responseSpec, AuthenticationResponseDto.class)
    }

    static AuthenticationResponseDto createAccount(final IntegrationTestBase testContext,
                                                   final CreateAccountRequestDto request) {
        final WebTestClient.ResponseSpec responseSpec = testContext.testClient()
                .post()
                .uri(ApiPaths.API_V1_ACCOUNT)
                .body(BodyInserters.fromObject(request))
                .exchange()

        return HttpTransportUtils.expect2xxBody(responseSpec, AuthenticationResponseDto.class)
    }

    static void deleteAllAccounts() {
        final userRepo = ApplicationContextHolder.getBean(UserRepository.class)
        final membersRepo = ApplicationContextHolder.getBean(MembershipRepository.class)
        final logRepo = ApplicationContextHolder.getBean(AccessLogRepository.class)

        userRepo.deleteAll()
        membersRepo.deleteAll()
        logRepo.deleteAll()
    }
}
