/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.persistence.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
class LoginServiceTest {
    private lateinit var sut: LoginService
    @Mock
    private lateinit var mockUserRepo: UserRepository
    @Mock
    private lateinit var mockAuthBusiness: AuthenticationBusiness

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.sut = LoginServiceImpl(mockUserRepo, mockAuthBusiness)
    }

    // `Nothing happens upon login if GUEST user's access token is valid`

    // `GUEST user's expired access token is renewed upon login`

    // `BASIC user's access token is always refreshed upon login`
}
