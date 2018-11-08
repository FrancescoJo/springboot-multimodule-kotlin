/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.LoginRequestDto
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.exception.account.UserNotFoundException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.vo.account.AccessToken
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@AllOpen
@Service
class LoginServiceImpl @Inject constructor(
        private val userRepo: UserRepository,
        private val authBusiness: AuthenticationBusiness
) : LoginService {
    override fun guestLogin(accessToken: AccessToken, request: LoginRequestDto): AuthenticationResponseDto {
        val user = userRepo.findByGuestCredential(accessToken.raw.toByteArray()).ensure()
        try {
            authBusiness.authenticate(accessToken)
            LOG.trace("User's access token is not expired and no renewal is needed.")
        } catch (e: AuthTokenExpiredException) {
            val newToken = authBusiness.createAccessToken(user)
            LOG.trace("User's access token is expired and issued a new one.")
            user.setAccessToken(newToken)
            userRepo.save(user)
        }

        return AuthenticationResponseDto.create(user)
    }

    override fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto {
        val user = userRepo.findByBasicCredential(requireNotNull(request.username),
                authBusiness.hash(request.credential.value.toByteArray())).ensure()

        /*
         * Because user is already authenticated so just issuing a new Access Token is cheaper
         * than checking its expected life.
         */
        val newToken = authBusiness.createAccessToken(user)
            user.setAccessToken(newToken)
            userRepo.save(user)

        return AuthenticationResponseDto.create(user)
    }

    private fun Optional<User>.ensure(): User {
        if (!isPresent) {
            throw UserNotFoundException("User is not found.")
        }

        return get()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LoginService::class.java)
    }
}
