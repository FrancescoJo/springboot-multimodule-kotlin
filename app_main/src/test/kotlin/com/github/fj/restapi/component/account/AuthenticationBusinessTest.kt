/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.lib.collection.getRandomBytes
import com.github.fj.lib.time.LOCAL_DATE_TIME_MAX
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.exception.AuthTokenException
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import com.github.fj.restapi.persistence.repository.UserRepository
import com.github.fj.restapi.vo.account.AccessToken
import com.google.common.io.BaseEncoding
import io.seruco.encoding.base62.Base62
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.restapi.account.AccountRequestUtils
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomAccessToken
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomUser
import java.util.*
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
class AuthenticationBusinessTest {
    private lateinit var sut: AuthenticationBusiness
    @Mock
    private lateinit var mockAppProperties: AppProperties
    @Mock
    private lateinit var mockUserRepository: UserRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.sut = AuthenticationBusinessImpl(mockAppProperties, mockUserRepository)
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
    @EnumSource(AccessToken.Encoded::class, names = ["FORWARD", "BACKWARD"])
    fun `Created access tokens must be decoded correctly`(mode: AccessToken.Encoded) {
        // given:
        val user = AccountRequestUtils.newRandomUser()
        val aes256Key = getRandomBytes(32)

        // and:
        (sut as AuthenticationBusinessImpl).accessTokenMode = mode
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(aes256Key)
        val issuedToken = sut.createAccessToken(user)
        user.authIv = issuedToken.iv.toByteArray()
        val issuedRawToken = issuedToken.raw.toByteArray()
        val userToken = Base62.createInstance().encode(issuedRawToken).toString(Charsets.UTF_8)
        `when`(mockUserRepository.findByAccessToken(issuedRawToken))
                .thenReturn(Optional.of(user))

        // when:
        val decodedToken = sut.parseAccessToken(userToken)

        // then:
        assertEquals(issuedToken, decodedToken)
    }

    @ParameterizedTest
    @MethodSource("createTamperedAccessTokens")
    fun `Authentication must be failed for tampered access tokens`(token: AccessToken) {
        assertThrows<AuthTokenException> {
            sut.authenticate(token)
        }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun createTamperedAccessTokens(): Stream<AccessToken> {
            val ownerUser = newRandomUser()
            return Stream.of(
                    newRandomAccessToken(ownerUser).apply { user = null },
                    newRandomAccessToken(ownerUser).apply {
                        user = newRandomUser().apply { idToken = "" }
                    },
                    newRandomAccessToken(ownerUser).apply {
                        user = newRandomUser().apply {
                            idToken = ownerUser.idToken
                            platformType = PlatformType.UNDEFINED
                            loginType = LoginType.UNDEFINED
                        }
                    },
                    newRandomAccessToken(ownerUser).apply {
                        user = newRandomUser().apply {
                            idToken = ownerUser.idToken
                            platformType = ownerUser.platformType
                            loginType = ownerUser.loginType
                            createdDate = LOCAL_DATE_TIME_MIN
                        }
                    },
                    newRandomAccessToken(ownerUser, requireNotNull(ownerUser.createdDate)).apply {
                        user = newRandomUser().apply {
                            idToken = ownerUser.idToken
                            platformType = ownerUser.platformType
                            loginType = ownerUser.loginType
                            createdDate = requireNotNull(ownerUser?.createdDate)
                        }
                    },
                    newRandomAccessToken(ownerUser, LOCAL_DATE_TIME_MAX).apply {
                        user = newRandomUser().apply {
                            idToken = ownerUser.idToken
                            platformType = ownerUser.platformType
                            loginType = ownerUser.loginType
                            createdDate = requireNotNull(user?.createdDate)
                        }
                    }
            )
        }
    }
}
