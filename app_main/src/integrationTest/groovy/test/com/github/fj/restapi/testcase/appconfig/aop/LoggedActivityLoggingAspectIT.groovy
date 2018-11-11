/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.testcase.appconfig.aop

import com.github.fj.restapi.persistence.repository.AccessLogRepository
import org.springframework.beans.factory.annotation.Autowired
import test.com.github.fj.restapi.IntegrationTestBase
import test.com.github.fj.restapi.util.AccountUtils

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
class LoggedActivityLoggingAspectIT extends IntegrationTestBase {
    @Autowired
    private AccessLogRepository logRepo

    def "in/out result of CreateAccountController should be logged"() {
        // Assumes that create account is annotated with @LoggedActivity
        when:
        AccountUtils.createRandomAccount(this)

        then:
        logRepo.count() == 1
    }

    def cleanup() {
        AccountUtils.deleteAllAccounts()
    }
}
