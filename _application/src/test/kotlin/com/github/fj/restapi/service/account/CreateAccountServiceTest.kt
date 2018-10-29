/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.exception.account.AccountAlreadyExistException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.service.account.AccountRequestHelper.newRandomCreateAccountRequest
import com.github.fj.restapi.service.account.AccountRequestHelper.newRandomUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.converter.HttpMessageNotReadableException
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
class CreateAccountServiceTest {
    private lateinit var sut: CreateAccountService
    private lateinit var mockUserRepo: UserRepository
    private lateinit var mockServletRequest: HttpServletRequest

    @BeforeEach
    fun setup() {
        this.mockUserRepo = mock(UserRepository::class.java)
        this.mockServletRequest = mock(HttpServletRequest::class.java)
        this.sut = CreateAccountService(mockUserRepo)
    }

    @Test
    fun `CreateAccountService fails for unknown login type`() {
        // given:
        val request = newRandomCreateAccountRequest(LoginType.UNDEFINED)

        // expect:
        assertThrows<HttpMessageNotReadableException> {
            sut.createAccount(request, mockServletRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(LoginType::class,
            mode = EnumSource.Mode.EXCLUDE,
            names = ["UNDEFINED"]
    )
    fun `CreateAccountService fails for already existing user`(loginType: LoginType) {
        // given:
        val req = newRandomCreateAccountRequest(loginType)
        val credentialArray = req.credential.toByteArray()

        // when:
        `when`(mockUserRepo.findByGuestCredential(credentialArray))
                .thenReturn(Optional.of(newRandomUser()))
        `when`(mockUserRepo.findByBasicCredential(req.username ?: "", credentialArray))
                .thenReturn(Optional.of(newRandomUser()))

        // then:
        assertThrows<AccountAlreadyExistException> {
            sut.createAccount(req, mockServletRequest)
        }
    }
}
