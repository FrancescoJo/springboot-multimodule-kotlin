/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.appconfig.aop

import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
class LoggedActivityLoggingAspectIT extends IntegrationTestBase {
    def "in/out result of CreateAccountController should be logged"() {
        when:
        final result = AccountUtils.createRandomAccount(this)

        then:
        result != null

        //        then:
        //        println(result)
    }
}
