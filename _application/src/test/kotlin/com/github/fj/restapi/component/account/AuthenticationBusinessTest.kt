/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

import com.github.fj.lib.collection.getRandomBytes
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.dto.account.AccessToken
import com.github.fj.restapi.service.account.AccountRequestHelper
import com.google.common.io.BaseEncoding
import io.seruco.encoding.base62.Base62
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
class AuthenticationBusinessTest {
    private lateinit var sut: AuthenticationBusiness
    private lateinit var mockAppProperties: AppProperties
    private lateinit var mockServletRequest: HttpServletRequest

    @BeforeEach
    fun setup() {
        this.mockAppProperties = mock(AppProperties::class.java)
        this.mockServletRequest = mock(HttpServletRequest::class.java)
        this.sut = AuthenticationBusinessImpl(mockAppProperties)
    }

    @Test
    fun `Hash salt result produces some hash`() {
        // given:
        val input = getRandomBytes(32)

        // when:
        val hashed = sut.hash(input)

        // then:
        assert(BaseEncoding.base16().encode(hashed).isNotEmpty())
    }

    @ParameterizedTest
    @EnumSource(AccessToken.Encoded::class, names = ["FORWARD", "BACKWARD"])
    fun `createAccessToken demo for each modes`(mode: AccessToken.Encoded) {
        // given:
        val user = AccountRequestHelper.newRandomUser()
        val aes256Key = getRandomBytes(32)

        // and:
        (sut as AuthenticationBusinessImpl).accessTokenMode = mode
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(aes256Key)

        // when:
        val myAuth = sut.createAccessToken(user)
        val old = requireNotNull(myAuth.accessToken)

        // and:
        val new = sut.parseAccessToken(Base62.createInstance()
                .encode(myAuth.rawAccessToken).toString(Charsets.UTF_8))

        // then:
        println("" + myAuth.rawAccessToken + " vs " + new.raw)
        assertEquals(old.mode, new.mode)
        println("" + old.iv + " vs " + new.iv)
        assertEquals(old.userId, new.userId)
        assertEquals(old.uIdTokenHash, new.uIdTokenHash)
        assertEquals(old.loginPlatformHash, new.loginPlatformHash)
        assertEquals(old.issuedTimestamp, new.issuedTimestamp)
        assertEquals(old.userRegisteredTimestamp, new.userRegisteredTimestamp)
    }
}
