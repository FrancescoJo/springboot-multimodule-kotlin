/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.HttpRequestHelper
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.dto.account.AccessToken
import com.github.fj.restapi.exception.account.AccountAlreadyExistException
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.service.account.AccountRequestHelper.newRandomCreateAccountRequest
import com.github.fj.restapi.service.account.AccountRequestHelper.newRandomUser
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.converter.HttpMessageNotReadableException
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
class CreateAccountServiceTest {
    private lateinit var sut: CreateAccountService
    private lateinit var mockUserRepo: UserRepository
    private lateinit var mockAuthBusiness: AuthenticationBusiness

    @BeforeEach
    fun setup() {
        this.mockUserRepo = mock(UserRepository::class.java)
        this.mockAuthBusiness = mock(AuthenticationBusiness::class.java)
        this.sut = CreateAccountServiceImpl(mockUserRepo, mockAuthBusiness)
    }

    @Test
    fun `CreateAccountService fails for unknown login type`() {
        // given:
        val request = newRandomCreateAccountRequest(LoginType.UNDEFINED)

        // expect:
        assertThrows<HttpMessageNotReadableException> {
            sut.createAccount(request, HttpRequestHelper.newMockHttpServletRequestByLocalhost())
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

        // and:
        `when`(mockUserRepo.findByGuestCredential(credentialArray))
                .thenReturn(Optional.of(newRandomUser()))
        `when`(mockUserRepo.findByBasicCredential(req.username ?: "", credentialArray))
                .thenReturn(Optional.of(newRandomUser()))

        // expect:
        assertThrows<AccountAlreadyExistException> {
            sut.createAccount(req, HttpRequestHelper.newMockHttpServletRequestByLocalhost())
        }
    }

    @Test
    fun `CreateAccountService passes if request represents a new user`() {
        // given:
        val req = newRandomCreateAccountRequest()
        val httpReq = HttpRequestHelper.newMockHttpServletRequestByLocalhost()
        val credentialArray = req.credential.toByteArray()

        // and:
        `when`(mockUserRepo.findByGuestCredential(credentialArray))
                .thenReturn(Optional.empty())
        `when`(mockUserRepo.findByBasicCredential(req.username ?: "", credentialArray))
                .thenReturn(Optional.empty())
        @Suppress("UnstableApiUsage")
        req.credential.toByteArray().let {
            `when`(mockAuthBusiness.hash(it))
                    .thenReturn(com.google.common.hash.Hashing.goodFastHash(it.size * 8).hashBytes(it).asBytes())
        }
        `when`(mockAuthBusiness.createAccessToken(any())).thenReturn(AccessToken.EMPTY)

        // when:
        val actual = sut.createAccount(req, httpReq)

        // then:
        assertEquals(req.nickname, actual.nickname)
        assertEquals(Gender.UNDEFINED, actual.gender)
        assertEquals(Status.NORMAL, actual.status)
    }
}
