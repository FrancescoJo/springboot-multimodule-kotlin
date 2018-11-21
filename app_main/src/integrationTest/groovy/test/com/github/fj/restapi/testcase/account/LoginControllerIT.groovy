/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.lib.RandomHelper
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
        final loginRequest = new LoginRequestDto(
                /*username =*/        "",
                /*credential =*/      new ProtectedProperty(""),
                /*loginType =*/       LoginType.GUEST,
                /*platformType =*/    RandomHelper.randomEnumConst(PlatformType.class),
                /*platformVersion =*/ AccountRequestUtils.DEFAULT_PLATFORM_VERSION,
                /*appVersion =*/      AccountRequestUtils.DEFAULT_APP_VERSION
        )
        final token = AccountUtils.createRandomAccount(this, LoginType.GUEST).accessToken.value

        when:
        final loginResult = testClient(token)
                .patch()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(loginRequest))
                .exchange()

        then:
        println(loginResult)
    }
}
