/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.account.DeleteAccountRequestDto
import com.github.fj.restapi.dto.account.DeleteAccountResponseDto
import com.github.fj.restapi.dto.account.ProfileResponseDto
import com.github.fj.restapi.dto.hello.HelloResponseDto
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.entity.User
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@RestController
class GetAccountController : IGetAccountController {
    override fun onGet(user: User): ProfileResponseDto {
        return ProfileResponseDto(
                id = user.idToken,
                membershipLevel = user.role.key,
                joinedSince = user.createdDate,
                nickname = user.member.nickname,
                gender = Gender.UNDEFINED,
                lastActive = user.member.lastActiveTimestamp,
                suspendedOn = user.member.suspendedOn,
                suspendedUntil = user.member.suspendedUntil
        )
    }
}
