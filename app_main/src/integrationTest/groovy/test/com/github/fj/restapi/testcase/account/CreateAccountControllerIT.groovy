/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account


import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.account.Status
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

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
        when:
        final result = AccountUtils.createRandomAccount(this)

        then:
        !result.accessToken.value.empty
        result.status == Status.NORMAL
    }
}
