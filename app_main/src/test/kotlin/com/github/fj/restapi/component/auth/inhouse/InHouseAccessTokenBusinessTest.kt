/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth.inhouse

import com.github.fj.lib.collection.getRandomBytes
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.component.auth.AccessTokenBusiness
import com.github.fj.restapi.component.auth.inhouse.Encoding.FORWARD
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.exception.account.UnknownAuthTokenException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.google.common.io.BaseEncoding
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.restapi.account.AccountRequestUtils
import test.com.github.fj.restapi.account.AccountRequestUtils.copyOf
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomUser
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
class InHouseAccessTokenBusinessTest {
    private lateinit var sut: AccessTokenBusiness
    @Mock
    private lateinit var mockAppProperties: AppProperties
    @Mock
    private lateinit var mockUserRepository: UserRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.sut = InhouseAccessTokenBusinessImpl(mockAppProperties, mockUserRepository)
    }

    @Test
    fun `Hash function results a non-null hash`() {
        // given:
        val input = getRandomBytes(32)

        // when:
        val hashed = sut.hash(input)

        // then:
        assert(BaseEncoding.base16().encode(hashed).isNotEmpty())
    }

    @ParameterizedTest
    @EnumSource(Encoding::class, names = ["FORWARD", "BACKWARD"])
    fun `Created access tokens must be decoded correctly`(mode: Encoding) {
        // given:
        val user = AccountRequestUtils.newRandomUser()
        val aes256Key = getRandomBytes(32)

        // and:
        (sut as InhouseAccessTokenBusinessImpl).accessTokenMode = mode
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(aes256Key)
        `when`(mockUserRepository.findByIdToken(user.idToken)).thenReturn(Optional.of(user))
        `when`(mockAppProperties.accessTokenAliveSecs).thenReturn(AppProperties.TOKEN_ALIVE_DURATION_SECS)
        val issuedToken = sut.create(user)

        // when:
        val decodedToken = sut.validate(issuedToken)
        val decodedUser = decodedToken.details as User

        // then:
        assertEquals(issuedToken, decodedToken.principal)
        assertEquals(user.idToken, decodedUser.idToken)
    }

    @ParameterizedTest
    @EnumSource(Encoding::class, names = ["FORWARD", "BACKWARD"])
    fun `Token is rejected if it represents no one`(mode: Encoding) {
        // given:
        val user = AccountRequestUtils.newRandomUser()
        val aes256Key = getRandomBytes(32)

        // and:
        (sut as InhouseAccessTokenBusinessImpl).accessTokenMode = mode
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(aes256Key)
        val issuedToken = sut.create(user)

        // when:
        assertThrows<UnknownAuthTokenException> {
            sut.validate(issuedToken)
        }
    }

    @ParameterizedTest
    @MethodSource("createTamperedAccessTokens")
    fun `Authentication must be failed for tampered access tokens`(key: ByteArray, expectedUser: User, token: String) {
        // given:
        (sut as InhouseAccessTokenBusinessImpl).accessTokenMode = FORWARD

        // and:
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(key)
        `when`(mockUserRepository.findByIdToken(any())).thenReturn(Optional.of(expectedUser))

        // expect:
        assertThrows<AuthTokenException> {
            sut.validate(token)
        }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun createTamperedAccessTokens(): Stream<Arguments> {
            // given:
            val aes256Key = ByteBuffer.allocate(32).apply {
                for (i in 0 until 32) {
                    put(i.toByte())
                }
            }.array()
            val mockAppProps = mock(AppProperties::class.java)
            val mockUserRepo = mock(UserRepository::class.java)
            val ownerUser = newRandomUser()

            // and:
            `when`(mockAppProps.accessTokenAes256Key).thenReturn(aes256Key)
            val mockedSut = InhouseAccessTokenBusinessImpl(mockAppProps, mockUserRepo).apply {
                accessTokenMode = FORWARD
            }

            // then:
            val originalToken = mockedSut.create(ownerUser)

            return Stream.of(
                    // ID Token info is tampered
                    Arguments.of(aes256Key, copyOf(ownerUser).apply {
                        idToken = ""
                    }, originalToken),
                    // Login type info is tampered
                    Arguments.of(aes256Key, copyOf(ownerUser).apply {
                        platformType = PlatformType.UNDEFINED
                        loginType = LoginType.UNDEFINED
                    }, originalToken),
                    // Registered date info is tampered
                    Arguments.of(aes256Key, copyOf(ownerUser).apply {
                        createdDate = LOCAL_DATE_TIME_MIN
                    }, originalToken),
                    // Token from the future
                    Arguments.of(aes256Key, copyOf(ownerUser), mockedSut.create(ownerUser, utcNow().plusDays(1))),
                    // Token is expired
                    Arguments.of(aes256Key, copyOf(ownerUser), mockedSut.create(ownerUser,
                            utcNow().minusSeconds(AppProperties.TOKEN_ALIVE_DURATION_SECS + 1.toLong())))
            )
        }
    }
}
