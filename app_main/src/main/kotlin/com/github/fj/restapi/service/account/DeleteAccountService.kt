/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.dto.account.DeleteAccountRequestDto
import com.github.fj.restapi.dto.account.DeleteAccountResponseDto
import com.github.fj.restapi.persistence.entity.User

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
interface DeleteAccountService {
    fun deleteAccount(user: User, deleteReason: DeleteAccountRequestDto?): DeleteAccountResponseDto
}
