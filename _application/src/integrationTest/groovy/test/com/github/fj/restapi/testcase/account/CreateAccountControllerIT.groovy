/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.dto.OkResponseDto
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.service.account.AccountRequestHelper
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
class CreateAccountControllerIT extends IntegrationTestBase {
    def "Request should be rejected with required fields are missing"() {
        given: "Required field 'appVersion' is missing"
        final request = """{
            "pushToken": "pBuzm5sxTs0cbTOMvI8x4qEbVwuacG2NzCnqoqPExmoiewZRnSOhrfF9WxRuOtL",
            "username":null,
            "credential":"IdOezP7FllwW8yFZXLOXVzL3oMSx3GRt6CcFWYS8SZqUssEKAYeeickgwxC1pXG",
            "nickname":"B88vb6B5KRSPVPCT",
            "gender":null,
            "loginType":"b",
            "platformType":"w",
            "platformVersion":"Platform-1.0",
            "email":"tester@testcompany.com"}
        """

        when:
        final responseSpec = testClient()
                .post()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(request))
                .exchange()

        then:
        responseSpec.expectStatus().is4xxClientError()
    }

    def "Request should be accepted with good request"() {
        given:
        final request = AccountRequestHelper.newRandomCreateAccountRequest()
        final responseSpec = testClient()
                .post()
                .uri("${ApiPaths.API_V1_ACCOUNT}")
                .body(BodyInserters.fromObject(request))

        when:
        final result = responseSpec.exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OkResponseDto).returnResult().responseBody.with extractResponseAs(AuthenticationResponseDto)

        then:
        !result.accessToken.empty
        result.nickname == request.nickname
        result.gender == Gender.UNDEFINED && request.gender == null
        result.status == Status.NORMAL
    }
}
