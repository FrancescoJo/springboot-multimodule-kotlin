/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.exception.account.AccountAlreadyExistException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.http.converter.HttpMessageNotReadableException
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
    fun createAccount(req: CreateAccountRequestDto, httpReq: HttpServletRequest):
            AuthenticationResponseDto {
        val maybeUser = when (req.loginType) {
            LoginType.GUEST -> {
                userRepo.findByGuestCredential(req.credential.toByteArray())
            }
            LoginType.BASIC -> {
                userRepo.findByBasicCredential(req.username ?: "", req.credential.toByteArray())
            }
            else -> {
                throw HttpMessageNotReadableException("${req.loginType} login is not supported.")
            }
        }

        if (maybeUser.isPresent) {
            throw AccountAlreadyExistException("Account already exists.")
        }

        // TODO: [3] Convert request to User + Member

        // TODO: [4] AuthenticationService: Bake token to user + track salt

        // TODO: [6] Service: repository.save()

        // TODO: [7] return service.createUserFrom(request, httpServletRequest)

        TODO("UNDER DEVELOPMENT") //To change body of created functions use File | Settings | File Templates.
    }
}
