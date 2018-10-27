/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.stereotype.Service
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Service
class CreateAccountService @Inject constructor(private val userRepo: UserRepository) {
    fun createAccount(accountRequest: CreateAccountRequestDto, httpRequest: HttpServletRequest):
            AuthenticationResponseDto {
        // TODO: [1] Search same identity before saving
        // (b: name + credential // other: based on login type and credential)

        // TODO: [2] HTTP 403 if same identity is already registered

        // TODO: [3] Convert request to User + Member

        // TODO: [4] AuthenticationService: Bake token to user + track salt

        // TODO: [6] Service: repository.save()

        // TODO: [7] return service.createUserFrom(request, httpServletRequest)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
