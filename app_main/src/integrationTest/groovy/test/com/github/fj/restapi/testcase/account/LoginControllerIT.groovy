/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.lib.RandomHelper
import test.com.github.fj.restapi.HttpTransportUtils
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.account.AccountRequestUtils
import test.com.github.fj.restapi.util.AccountUtils

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Nov - 2018
 */
class LoginControllerIT extends IntegrationTestBase {
    def "Guest login always require access token as request header."() {
        given:
        final loginType = LoginType.GUEST
        final loginRequest = new LoginRequestDto(
                /*username =*/        "",
                /*credential =*/      new ProtectedProperty(""),
                /*loginType =*/       loginType,
                /*platformType =*/    RandomHelper.randomEnumConst(PlatformType.class),
                /*platformVersion =*/ AccountRequestUtils.DEFAULT_PLATFORM_VERSION,
                /*appVersion =*/      AccountRequestUtils.DEFAULT_APP_VERSION
        )
        final accountResult = AccountUtils.createRandomAccount(this, loginType)
        final id = accountResult.id
        final nickname = accountResult.nickname
        final gender = accountResult.gender
        final token = accountResult.accessToken.value

        when:
        final responseSpec = testClient(token)
                .patch()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(loginRequest))
                .exchange()

        then:
        responseSpec.expectStatus().is2xxSuccessful()

        when:
        final loginResponse = HttpTransportUtils.expect2xxBody(responseSpec, AuthenticationResponseDto.class)

        then:
        loginType == loginResponse.loginType
        id == loginResponse.id
        nickname == loginResponse.nickname
        gender == loginResponse.gender
    }

    def "Basic login always require preregistered credential. No auth header is needed."() {
        given:
        final loginType = LoginType.BASIC
        final createRequest = AccountRequestUtils.newRandomCreateAccountRequest(loginType)
        final username = createRequest.username
        final password = createRequest.credential.value
        final platformType = createRequest.platformType
        final platformVer = createRequest.platformVersion
        final appVer = createRequest.appVersion

        and:
        final accountResult = AccountUtils.createAccount(this, createRequest)
        final id = accountResult.id
        final nickname = accountResult.nickname
        final gender = accountResult.gender
        final loginRequest = new LoginRequestDto(
                /*username =*/        username,
                /*credential =*/      new ProtectedProperty(password),
                /*loginType =*/       loginType,
                /*platformType =*/    platformType,
                /*platformVersion =*/ platformVer,
                /*appVersion =*/      appVer
        )

        when:
        final responseSpec = testClient()
                .patch()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(loginRequest))
                .exchange()

        then:
        responseSpec.expectStatus().is2xxSuccessful()

        when:
        final loginResponse = HttpTransportUtils.expect2xxBody(responseSpec, AuthenticationResponseDto.class)

        then:
        loginType == loginResponse.loginType
        id == loginResponse.id
        nickname == loginResponse.nickname
        gender == loginResponse.gender
    }
}
