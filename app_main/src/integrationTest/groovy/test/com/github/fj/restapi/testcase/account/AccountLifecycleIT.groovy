/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.account

import com.github.fj.restapi.persistence.consts.account.Status
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Nov - 2018
 */
class AccountLifecycleIT extends IntegrationTestBase {
    def "All the account lifecycle activities should be done and logged flawlessly"() {
        String accessToken
        // region Account creation
        when:
        final result = AccountUtils.createRandomAccount(this)
        accessToken = result.accessToken.value

        then: "Account should be created"
        !accessToken.empty
        result.status == Status.NORMAL
        // endregion
    }

    // account create - login - get - delete
}
