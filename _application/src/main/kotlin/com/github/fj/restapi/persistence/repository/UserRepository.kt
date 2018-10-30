/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository

import com.github.fj.restapi.persistence.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Repository
interface UserRepository : CrudRepository<User, Long> {
    @Query("""
        SELECT u
        FROM User u
        WHERE u.loginType = com.github.fj.restapi.persistence.consts.account.LoginType.BASIC
          AND u.name = ?1
          AND u.credential = ?2
    """)
    fun findByBasicCredential(name: String, binaryCredential: ByteArray): Optional<User>

    @Query("""
        SELECT u
        FROM User u
        WHERE u.loginType = com.github.fj.restapi.persistence.consts.account.LoginType.GUEST
          AND u.credential = ?1
    """)
    fun findByGuestCredential(binaryCredential: ByteArray): Optional<User>

    @Query("""
        SELECT u
        FROM User u
        WHERE u.idToken = ?1
    """)
    fun findByIdToken(idToken: String): Optional<User>
}
