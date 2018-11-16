/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.time.LOCAL_DATE_TIME_MAX
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountRequestDto
import com.github.fj.restapi.endpoint.v1.account.dto.DeleteAccountResponseDto
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.stereotype.Service
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@AllOpen
@Service
class DeleteAccountServiceImpl @Inject constructor(
        private val userRepo: UserRepository
) : DeleteAccountService {
    override fun deleteAccount(user: User, deleteReason: DeleteAccountRequestDto?):
            DeleteAccountResponseDto {
        user.run {
            status = Status.WITHDRAWN
            member.suspendedOn = utcNow()
            member.suspendedUntil = LOCAL_DATE_TIME_MAX
        }

        userRepo.save(user)
        return DeleteAccountResponseDto("Goodbye")
    }
}
