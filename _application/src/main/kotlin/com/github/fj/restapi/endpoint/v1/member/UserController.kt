/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.member

import com.github.fj.restapi.dto.v1.member.UserResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@RestController
class UserController : IUserController {
    @PreAuthorize("hasRole('USER')")
    override fun onGetUser(): UserResponseDto {
        // TODO: How could we retrieve user information at here? via SecurityContextHolder#getContext()??
        return UserResponseDto("user", "USER")
    }
}
