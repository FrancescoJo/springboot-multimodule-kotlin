/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.restapi.dto.account.AuthenticationResponseDto
import com.github.fj.restapi.dto.hello.HelloResponseDto
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@RestController
class LoginController : ILoginController {
    override fun onPatch(): AuthenticationResponseDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
