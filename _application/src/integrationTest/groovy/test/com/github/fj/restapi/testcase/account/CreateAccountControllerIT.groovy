/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.helper.account.AccountControllerRequestHelper

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
                .uri("/${ApiPaths.ACCOUNT}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(request))
                .exchange()

        then:
        responseSpec.expectStatus().is4xxClientError()
    }

    def "Request should be accepted with good request"() {
        given:
        final request = AccountControllerRequestHelper.newRandomCreateAccountRequest()

        when:
        final responseSpec = testClient()
                .post()
                .uri("/${ApiPaths.ACCOUNT}")
                .body(BodyInserters.fromObject(request))
                .exchange()

        then:
        responseSpec.expectStatus().is2xxSuccessful()
        // TODO: Parse expected message at the end of this test
        //                 .expectBody(OkResponseDto).returnResult().responseBody.with extractResponseAs(HelloResponseDto)
        //        response.message == "POST Hello, MY_NAME"
    }
}
