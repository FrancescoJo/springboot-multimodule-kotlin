/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.lib.time.DateTimeUtilsKt
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountRequestDto
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

import java.time.temporal.ChronoUnit

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
class DeleteAccountControllerIT extends IntegrationTestBase {
    @Autowired
    private UserRepository userRepository

    def "Account is deleted by genuine access Token"() {
        given:
        final accountInfo = AccountUtils.createRandomAccount(this)

        and:
        final maybeCreatedUser = userRepository.findAll().stream()
                .filter({ user -> user.idToken == accountInfo.id })
                .findFirst()

        expect:
        maybeCreatedUser.isPresent()

        and:
        final createdUser = maybeCreatedUser.get()
        final deleteRequestTime = DateTimeUtilsKt.utcNow().truncatedTo(ChronoUnit.SECONDS)

        when:
        final responseSpec = makeResponseSpec(accountInfo.accessToken.value)

        and:
        final maybeUser = userRepository.findByIdToken(accountInfo.id)
        final deletedUser = userRepository.findById(createdUser.id)

        then:
        responseSpec.expectStatus().is2xxSuccessful()
        !maybeUser.isPresent()
        deletedUser.isPresent()
        deletedUser.get().status == Status.WITHDRAWN
        deletedUser.get().member.suspendedOn >= deleteRequestTime
        deletedUser.get().member.suspendedUntil == DateTimeUtilsKt.LOCAL_DATE_TIME_MAX
    }

    private WebTestClient.ResponseSpec makeResponseSpec(final String accessToken) {
        final request = new DeleteAccountRequestDto("Test deletion")

        return testClient(accessToken)
                .method(HttpMethod.DELETE)
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(request))
                .exchange()
    }
}
