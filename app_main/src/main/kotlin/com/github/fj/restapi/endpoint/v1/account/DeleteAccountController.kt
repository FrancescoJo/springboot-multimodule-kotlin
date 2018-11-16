/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountRequestDto
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountResponseDto
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.service.account.DeleteAccountService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@RestController
class DeleteAccountController @Inject constructor(
        private val svc: DeleteAccountService
) : IDeleteAccountController {
    override fun onDelete(user: User, deleteReason: DeleteAccountRequestDto?):
            DeleteAccountResponseDto {
        LOG.debug("Delete account request: UserId {}", user.id)
        return svc.deleteAccount(user, deleteReason)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DeleteAccountController::class.java)
    }
}
