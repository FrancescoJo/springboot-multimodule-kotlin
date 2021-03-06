/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository

import com.github.fj.restapi.persistence.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("""
        SELECT u
        FROM User u
        WHERE u.loginType = com.github.fj.restapi.persistence.consts.account.LoginType.GUEST
          AND u.credential = ?1
          AND u.status = com.github.fj.restapi.persistence.consts.account.Status.NORMAL
    """)
    fun findByGuestCredential(binaryCredential: ByteArray): Optional<User>

    @Query("""
        SELECT u
        FROM User u
        WHERE u.loginType = com.github.fj.restapi.persistence.consts.account.LoginType.BASIC
          AND u.name = ?1
          AND u.credential = ?2
          AND u.status = com.github.fj.restapi.persistence.consts.account.Status.NORMAL
    """)
    fun findByBasicCredential(name: String, binaryCredential: ByteArray): Optional<User>

    @Query("""
        SELECT u
        FROM User u
        WHERE u.idToken = ?1
          AND u.status = com.github.fj.restapi.persistence.consts.account.Status.NORMAL
    """)
    fun findByIdToken(idToken: String): Optional<User>
}
