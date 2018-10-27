/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.account

import com.github.fj.restapi.dto.hello.HelloResponseDto
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@RestController
class DeleteAccountController : IDeleteAccountController {
    override fun onDelete(): HelloResponseDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
