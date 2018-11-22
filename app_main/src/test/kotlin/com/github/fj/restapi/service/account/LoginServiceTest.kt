/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.lib.text.getRandomAlphaNumericString
import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.auth.AccessTokenBusinessFactory
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.restapi.account.AccountRequestUtils.*
import test.com.github.fj.restapi.account.MockAuthentication
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
class LoginServiceTest {
    private lateinit var sut: LoginService
    @Mock
    private lateinit var mockUserRepo: UserRepository
    @Mock
    private lateinit var mockAccessTokenBusinessFactory: AccessTokenBusinessFactory
    @Mock
    private lateinit var mockTokenBusiness: AccessTokenBusiness

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockAccessTokenBusinessFactory.get()).thenReturn(mockTokenBusiness)
        this.sut = LoginServiceImpl(mockUserRepo, mockAccessTokenBusinessFactory)
    }

    @Test
    fun `Nothing happens upon login if GUEST user access token is valid`() {
        // given:
        val user = newRandomUser()
        val token = newRandomAccessToken()
        val req = newRandomLoginRequest(LoginType.GUEST)
        val mockAuthentication = MockAuthentication().apply {
            details = user
        }

        // and:
        `when`(mockTokenBusiness.validate(any())).thenReturn(mockAuthentication)

        // when:
        sut.guestLogin(token, req)

        // then:
        verify(mockUserRepo, times(0)).save(user)
    }

    @Test
    fun `Expired GUEST user access token is renewed upon login`() {
        // given:
        val user = newRandomUser()
        val token = newRandomAccessToken()
        val req = newRandomLoginRequest(LoginType.GUEST)
        val hashedCredential = ByteArray(0)

        // and:
        `when`(mockTokenBusiness.validate(token)).thenThrow(AuthTokenExpiredException())
        `when`(mockTokenBusiness.hash(token.toByteArray())).thenReturn(hashedCredential)
        `when`(mockUserRepo.findByGuestCredential(any())).thenReturn(Optional.of(user))
        val newToken = newRandomAccessToken()
        `when`(mockTokenBusiness.create(eq(user), any())).thenReturn(newToken)
        `when`(mockTokenBusiness.hash(newToken.toByteArray())).thenReturn(hashedCredential)
        `when`(mockUserRepo.save(user)).thenReturn(user)

        // when:
        sut.guestLogin(token, req)

        // then:
        verify(mockTokenBusiness, times(1)).create(eq(user), any())
        verify(mockUserRepo, times(1)).save(user)
        verify(mockUserRepo).save<User>(argThat {
            credential.contentEquals(hashedCredential)
        })
    }

    @Test
    fun `BASIC user access token is always refreshed upon login`() {
        // given:
        val user = newRandomUser()
        val request = newRandomLoginRequest(LoginType.BASIC)
        val newAccessToken = newRandomAccessToken()
        val hashedCredential = ByteArray(0)

        // and:
        `when`(mockTokenBusiness.hash(request.credential.value.toByteArray()))
                .thenReturn(hashedCredential)
        `when`(mockUserRepo.findByBasicCredential(request.username, hashedCredential))
                .thenReturn(Optional.of(user))
        `when`(mockTokenBusiness.create(eq(user), any())).thenReturn(newAccessToken)

        // when:
        val result = sut.basicLogin(request)

        // then:
        verify(mockTokenBusiness, times(1)).create(eq(user), any())
        assertEquals(newAccessToken, result.accessToken.value)
    }

    private fun newRandomAccessToken(): String = getRandomAlphaNumericString(84)
}
