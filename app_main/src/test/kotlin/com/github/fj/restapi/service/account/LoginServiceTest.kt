/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.auth.AccessTokenBusinessFactory
import com.github.fj.restapi.exception.account.AuthTokenExpiredException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.restapi.account.AccountRequestUtils.*
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
class LoginServiceTest {
    // TODO: Restore test
//    private lateinit var sut: LoginService
//    @Mock
//    private lateinit var mockUserRepo: UserRepository
//    @Mock
//    private lateinit var mockAccessTokenBusinessFactory: AccessTokenBusinessFactory
//    @Mock
//    private lateinit var mockAccessTokenBusiness: AccessTokenBusiness
//
//    @BeforeEach
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        `when`(mockAccessTokenBusinessFactory.get()).thenReturn(mockAccessTokenBusiness)
//        this.sut = LoginServiceImpl(mockUserRepo, mockAccessTokenBusinessFactory)
//    }
//
//    @Test
//    fun `Nothing happens upon login if GUEST user access token is valid`() {
//        // given:
//        val user = newRandomUser()
//        val token = newRandomAccessToken(user)
//        val req = newRandomLoginRequest(LoginType.GUEST)
//
//        // and:
//        `when`(mockUserRepo.findByGuestCredential(token.raw.toByteArray()))
//                .thenReturn(Optional.of(user))
//
//        // when:
//        sut.guestLogin(token, req)
//
//        // then:
//        verify(mockUserRepo, times(0)).save(user)
//    }
//
//    @Test
//    fun `Expired GUEST user access token is renewed upon login`() {
//        // given:
//        val user = newRandomUser()
//        val token = newRandomAccessToken(user)
//        val req = newRandomLoginRequest(LoginType.GUEST)
//
//        // and:
//        `when`(mockUserRepo.findByGuestCredential(token.raw.toByteArray()))
//                .thenReturn(Optional.of(user))
//        `when`(mockAccessTokenBusiness.validate(token)).thenThrow(AuthTokenExpiredException())
//        val newToken = newRandomAccessToken(user)
//        `when`(mockAccessTokenBusiness.create(eq(user), any())).thenReturn(newToken)
//
//        // when:
//        sut.guestLogin(token, req)
//
//        // then:
//        verify(mockAccessTokenBusiness, times(1)).create(eq(user), any())
//        verify(mockUserRepo, times(1)).save(user)
//        verify(mockUserRepo).save<User>(argThat { token.issuedTimestamp == newToken.issuedTimestamp })
//    }
//
//    @Test
//    fun `BASIC user access token is always refreshed upon login`() {
//        // given:
//        val user = newRandomUser()
//        val request = newRandomLoginRequest(LoginType.BASIC)
//        val savedCredential = ByteArray(0)
//        val newAccessToken = newRandomAccessToken(user)
//
//        // and:
//        `when`(mockAccessTokenBusiness.hash(request.credential.value.toByteArray()))
//                .thenReturn(savedCredential)
//        `when`(mockUserRepo.findByBasicCredential(request.username, savedCredential))
//                .thenReturn(Optional.of(user))
//        `when`(mockAccessTokenBusiness.create(eq(user), any())).thenReturn(newAccessToken)
//
//        // when:
//        sut.basicLogin(request)
//
//        // then:
//        verify(mockAccessTokenBusiness, times(1)).create(eq(user), any())
//    }
}
