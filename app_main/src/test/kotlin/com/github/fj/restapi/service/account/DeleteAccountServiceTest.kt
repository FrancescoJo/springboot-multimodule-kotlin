/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.service.account

import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.entity.User
import com.github.fj.restapi.persistence.repository.UserRepository
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.restapi.account.AccountRequestUtils.newRandomUser

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
class DeleteAccountServiceTest {
    private lateinit var sut: DeleteAccountService
    @Mock
    private lateinit var mockUserRepo: UserRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.sut = DeleteAccountServiceImpl(mockUserRepo)
    }

    @Test
    fun `delete account sets user state as Status#WITHDRAWN`() {
        // when:
        sut.deleteAccount(newRandomUser(), null)

        // expect:
        verify(mockUserRepo).save<User>(argThat { status == Status.WITHDRAWN })
    }
}
