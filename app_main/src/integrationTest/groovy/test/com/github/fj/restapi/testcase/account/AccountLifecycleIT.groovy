/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountRequestDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.endpoint.v1.account.dto.ProfileResponseDto
import com.github.fj.restapi.persistence.consts.account.LoginType
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.HttpTransportUtils
import test.com.github.fj.restapi.IntegrationTestBase

import static test.com.github.fj.restapi.account.AccountRequestUtils.newRandomCreateAccountRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Nov - 2018
 */
class AccountLifecycleIT extends IntegrationTestBase {
    def "All the account lifecycle activities should be done and logged flawlessly"() {
        given:
        final AuthenticationResponseDto authentication
        final String accessToken

        // region Account creation
        when:
        final accountRequest = newRandomCreateAccountRequest(loginType)
        final accountResult = testClient()
                .post()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(accountRequest))
                .exchange()

        then: "Account must be successfully created"
        accountResult.expectStatus().is2xxSuccessful()
        // endregion

        when:
        authentication = HttpTransportUtils.expect2xxBody(accountResult, AuthenticationResponseDto.class)
        accessToken = authentication.accessToken.value

        // region Login
        final loginRequest = new LoginRequestDto(
                /*username:*/        accountRequest.username,
                /*credential:*/      accountRequest.credential,
                /*loginType:*/       loginType,
                /*platformType:*/    accountRequest.platformType,
                /*platformVersion:*/ accountRequest.platformVersion,
                /*appVersion:*/      accountRequest.appVersion
        )
        final loginResult = testClient(accessToken)
                .patch()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(loginRequest))
                .exchange()

        then: "Login must be done successfully, with last known authentication"
        loginResult.expectStatus().is2xxSuccessful()
        // endregion

        // region Get profile
        when:
        final myProfileResult = testClient(accessToken)
                .method(HttpMethod.GET)
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
        final myProfile = HttpTransportUtils.expect2xxBody(myProfileResult, ProfileResponseDto.class)

        then:
        authentication.id == myProfile.id
        authentication.gender == myProfile.gender
        // endregion

        // region Delete profile
        when:
        final deleteResponseSpec = testClient(accessToken)
                .method(HttpMethod.DELETE)
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(new DeleteAccountRequestDto("AccountLifecycleIT deletion")))
                .exchange()

        then:
        deleteResponseSpec.expectStatus().is2xxSuccessful()
        // endregion

        where:
        loginType       | _
        LoginType.GUEST | _
        LoginType.BASIC | _
    }
}
