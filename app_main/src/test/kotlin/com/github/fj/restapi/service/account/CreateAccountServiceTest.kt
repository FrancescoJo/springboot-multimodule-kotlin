/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.exception.account.AccountAlreadyExistException
import com.github.fj.restapi.persistence.consts.account.Gender
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.vo.account.AccessToken
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.http.converter.HttpMessageNotReadableException
import test.com.github.fj.restapi.HttpTransportUtils
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomCreateAccountRequest
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomUser
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
class CreateAccountServiceTest {
    private lateinit var sut: CreateAccountService
    @Mock
    private lateinit var mockUserRepo: UserRepository
    @Mock
    private lateinit var mockAuthBusiness: AuthenticationBusiness

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.sut = CreateAccountServiceImpl(mockUserRepo, mockAuthBusiness)
    }

    @Test
    fun `CreateAccountService fails for unknown login type`() {
        // given:
        val request = newRandomCreateAccountRequest(LoginType.UNDEFINED)

        // expect:
        assertThrows<HttpMessageNotReadableException> {
            sut.createAccount(request, HttpTransportUtils.newMockHttpServletRequestByLocalhost())
        }
    }

    @ParameterizedTest
    @EnumSource(LoginType::class, names = ["BASIC"])
    fun `CreateAccountService fails for already existing user`(loginType: LoginType) {
        // given:
        val req = newRandomCreateAccountRequest(loginType)
        val credentialArray = req.credential.value.toByteArray()

        // and:
        `when`(mockAuthBusiness.hash(credentialArray)).thenReturn(ByteArray(0))
        `when`(mockUserRepo.findByBasicCredential(eq(req.username), any()))
                .thenReturn(Optional.of(newRandomUser()))

        // expect:
        assertThrows<AccountAlreadyExistException> {
            sut.createAccount(req, HttpTransportUtils.newMockHttpServletRequestByLocalhost())
        }
    }

    @ParameterizedTest
    @EnumSource(LoginType::class, names = ["BASIC", "GUEST"])
    fun `CreateAccountService passes if request represents a new user`(loginType: LoginType) {
        // given:
        val req = newRandomCreateAccountRequest(loginType)
        val httpReq = HttpTransportUtils.newMockHttpServletRequestByLocalhost()
        val credentialArray = req.credential.value.toByteArray()

        // and:
        `when`(mockUserRepo.findByBasicCredential(req.username, credentialArray))
                .thenReturn(Optional.empty())
        `when`(mockAuthBusiness.hash(credentialArray)).thenReturn(ByteArray(0))
        `when`(mockAuthBusiness.createAccessToken(any())).thenReturn(AccessToken.EMPTY)
        `when`(mockUserRepo.findByIdToken(req.invitedBy ?: ""))
                .thenReturn(Optional.empty())

        // when:
        val actual = sut.createAccount(req, httpReq)

        // then:
        assertEquals(req.nickname, actual.nickname)
        assertEquals(Gender.UNDEFINED, actual.gender)
        assertEquals(Status.NORMAL, actual.status)
    }

    @Test
    fun `invitedBy value is correctly saved if it points an existing user`() {
        // given:
        val req = newRandomCreateAccountRequest()
        val httpReq = HttpTransportUtils.newMockHttpServletRequestByLocalhost()
        val credentialArray = req.credential.value.toByteArray()

        // and:
        `when`(mockAuthBusiness.hash(credentialArray)).thenReturn(ByteArray(0))
        val existingUser = newRandomUser()
        `when`(mockUserRepo.findByIdToken(req.invitedBy ?: ""))
                .thenReturn(Optional.of(existingUser))
        `when`(mockAuthBusiness.createAccessToken(any())).thenReturn(AccessToken.EMPTY)

        // when:
        val actual = sut.createAccount(req, httpReq)

        // then:
        assertNotNull(actual)
        verify(mockUserRepo).save<User>(argThat { invitedBy == existingUser.id })
    }
}
