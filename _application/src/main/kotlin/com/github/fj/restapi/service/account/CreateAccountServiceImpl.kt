/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.text.SemanticVersion
import com.github.fj.lib.text.getRandomCapitalAlphaNumericString
import com.github.fj.lib.text.isNullOrUnicodeBlank
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.CreateAccountRequestDto
import com.github.fj.restapi.exception.account.AccountAlreadyExistException
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.entity.Member
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.util.extractIp
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Service
class CreateAccountServiceImpl @Inject constructor(
        private val userRepo: UserRepository,
        private val authBusiness: AuthenticationBusiness
) : CreateAccountService {
    override fun createAccount(req: CreateAccountRequestDto, httpReq: HttpServletRequest):
            AuthenticationResponseDto {
        val maybeUser = when (req.loginType) {
            LoginType.GUEST -> {
                userRepo.findByGuestCredential(req.credential.toByteArray())
            }
            LoginType.BASIC -> {
                val username = if (req.username.isNullOrUnicodeBlank()) {
                    throw HttpMessageNotReadableException("User name must not be empty or blank.")
                } else {
                    requireNotNull(req.username)
                }
                userRepo.findByBasicCredential(username, req.credential.toByteArray())
            }
            else -> throw HttpMessageNotReadableException("${req.loginType} login is not supported.")
        }

        if (maybeUser.isPresent) {
            throw AccountAlreadyExistException("Account already exists.")
        }

        val user = User().apply {
            val now = LocalDateTime.now()
            val ipAddr = InetAddress.getByName(httpReq.extractIp())

            var maybeUserWithIdToken: Optional<User>
            var newIdToken: String
            do {
                newIdToken = getRandomCapitalAlphaNumericString(ID_TOKEN_LENGTH)
                maybeUserWithIdToken = userRepo.findByIdToken(newIdToken)
            } while (maybeUserWithIdToken.isPresent)

            idToken = newIdToken
            status = Status.NORMAL
            // TODO: Assign role
            name = when (req.loginType) {
                LoginType.GUEST -> ""
                LoginType.BASIC -> requireNotNull(req.username)
                else -> throw UnsupportedOperationException("${req.loginType} login is not supported.")
            }
            loginType = req.loginType
            platformType = req.platformType
            platformVersion = req.platformVersion
            appVersion = SemanticVersion.parse(req.appVersion)
            email = req.email ?: ""
            createdDate = now
            createdIp = ipAddr
            pushToken = req.pushToken
            invitedBy = if (req.invitedBy.isNullOrUnicodeBlank()) {
                Optional.empty()
            } else {
                userRepo.findByIdToken(requireNotNull(req.invitedBy))
            }.let {
                return@let if (it.isPresent) {
                    it.get().id
                } else {
                    0
                }
            }
            credential = when (req.loginType) {
                LoginType.GUEST -> null
                LoginType.BASIC -> authBusiness.hash(req.credential.toByteArray())
                else -> throw UnsupportedOperationException("${req.loginType} login is not supported.")
            }
            member = Member().apply {
                nickname = req.nickname
                gender = req.gender ?: Gender.UNDEFINED
                lastActiveTimestamp = now
                lastActiveIp = ipAddr
            }
        }

        // TODO: [4] AuthenticationComponent: Bake token to user(various patterns) + track salt
        //           Ciphertext creation strategy: IV + Payload -> No padding Base62
        // TODO: [5] 1 to 1 User -> UserAuthentication: FK id + accessToken(254) + issuedSalt(254) + issuedDate + suspendedOn + suspendedUntil

        // TODO: [6] Service: repository.save()

        // TODO: [7] return service.createUserFrom(request, httpServletRequest)

        TODO("UNDER DEVELOPMENT") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val ID_TOKEN_LENGTH = 16
    }
}
