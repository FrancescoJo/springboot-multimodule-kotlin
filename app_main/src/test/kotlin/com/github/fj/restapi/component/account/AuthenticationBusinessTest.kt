/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.account

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
class AuthenticationBusinessTest {
    // TODO: Fix bug in backward + BASIC login case
    /*
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
        val user = AccountRequestUtils.newRandomUser()
        val aes256Key = getRandomBytes(32)

        // and:
        (sut as AuthenticationBusinessImpl).accessTokenMode = mode
        `when`(mockAppProperties.accessTokenAes256Key).thenReturn(aes256Key)

        // when:
        val token = sut.createAccessToken(user)
        val old = requireNotNull(token.raw).toByteArray()

        // and:
        val accessToken = Base62.createInstance().encode(old).toString(Charsets.UTF_8)
        val iv = token.iv.toByteArray()

        // when:
        val new = sut.parseAccessToken(accessToken, iv)

        // then:
        assertEquals(old, new)
    }
    */
}
