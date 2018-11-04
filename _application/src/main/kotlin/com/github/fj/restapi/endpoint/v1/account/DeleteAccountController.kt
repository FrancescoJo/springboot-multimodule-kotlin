/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.v1.account

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.dto.account.DeleteAccountRequestDto
import com.github.fj.restapi.dto.account.DeleteAccountResponseDto
import com.github.fj.restapi.dto.hello.HelloResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@RestController
class DeleteAccountController : IDeleteAccountController {
//    override fun onDelete(): DeleteAccountResponseDto = onDelete(null)

    override fun onDelete(deleteReason: DeleteAccountRequestDto?): DeleteAccountResponseDto {

        println(">>>> onDelete")


        TODO("onDelete: Under development")
    }
}
