/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.component.auth.AccessTokenBusinessFactory
import com.github.fj.restapi.endpoint.v1.account.dto.AuthenticationResponseDto
import com.github.fj.restapi.endpoint.v1.account.dto.LoginRequestDto
import com.github.fj.restapi.exception.AuthTokenException
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
internal class LoginServiceImpl @Inject constructor(
        private val userRepo: UserRepository,
        tokenBizFactory: AccessTokenBusinessFactory
) : LoginService {
    private val tokenBusiness = tokenBizFactory.get()

    override fun guestLogin(accessToken: String, request: LoginRequestDto):
            AuthenticationResponseDto {
        var user: User
        var newToken: String
        try {
            user = tokenBusiness.validate(accessToken).details as User
            LOG.trace("User's access token is not expired and no renewal is needed.")
            newToken = accessToken
        } catch (e: AuthTokenExpiredException) {
            user = with(ensureGuestUser(accessToken.toByteArray())) {
                newToken = tokenBusiness.create(this)
                LOG.trace("User's access token is expired and issued a new one.")
                this.credential = tokenBusiness.hash(newToken.toByteArray())
                return@with userRepo.save(this)
            }
        } catch (e: AuthTokenException) {
            LOG.trace("User not found for given access token.")
            throw UserNotFoundException("User is not found.", e)
        }

        return AuthenticationResponseDto.create(user, newToken)
    }

    override fun basicLogin(request: LoginRequestDto): AuthenticationResponseDto {
        val user = ensureUser(requireNotNull(request.username),
                request.credential.value.toByteArray())

        /*
         * Because user is already authenticated so just issuing a new Access Token is cheaper
         * than checking its expected life.
         */
        return AuthenticationResponseDto.create(user, tokenBusiness.create(user))
    }

    private fun ensureGuestUser(credential: ByteArray): User {
        return userRepo.findByGuestCredential(tokenBusiness.hash(credential))
                .takeIf { it.isPresent }?.get()
                ?: throw UserNotFoundException("User is not found.")
    }

    private fun ensureUser(username: String, credential: ByteArray): User {
        return userRepo.findByBasicCredential(username, tokenBusiness.hash(credential))
                .takeIf { it.isPresent }?.get()
                ?: throw UserNotFoundException("User is not found.")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LoginService::class.java)
    }
}
