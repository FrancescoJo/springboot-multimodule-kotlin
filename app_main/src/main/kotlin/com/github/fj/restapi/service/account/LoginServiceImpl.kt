/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.component.auth.AccessTokenBusinessFactory
import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.exception.account.UserNotFoundException
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@AllOpen
@Service
class LoginServiceImpl @Inject constructor(
        private val userRepo: UserRepository,
        tokenBizFactory: AccessTokenBusinessFactory
) : LoginService {
    private val tokenBusiness = tokenBizFactory.get()

    override fun guestLogin(accessToken: String, request: LoginRequestDto):
            AuthenticationResponseDto {
        val user = ensureUser("", accessToken.toByteArray())

        var newToken: String
        try {
            tokenBusiness.validate(accessToken)
            LOG.trace("User's access token is not expired and no renewal is needed.")
            newToken = accessToken
        } catch (e: AuthTokenExpiredException) {
            newToken = tokenBusiness.create(user)
            LOG.trace("User's access token is expired and issued a new one.")
            userRepo.save(user)
        }

        return AuthenticationResponseDto.create(user, newToken)
    }

    override fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto {
        val user = ensureUser(requireNotNull(request.username),
                tokenBusiness.hash(request.credential.value.toByteArray()))

        /*
         * Because user is already authenticated so just issuing a new Access Token is cheaper
         * than checking its expected life.
         */
        return AuthenticationResponseDto.create(user, tokenBusiness.create(user))
    }

    private fun ensureUser(username: String, credential: ByteArray): User {
        return userRepo.findByBasicCredential(username, credential)
                .takeIf { it.isPresent }?.get()
                ?: throw UserNotFoundException("User is not found.")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LoginService::class.java)
    }
}
