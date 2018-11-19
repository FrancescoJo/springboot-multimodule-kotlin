/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository.extended

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.persistence.entity.RsaKeyPair
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
@AllOpen
@Repository
internal class RsaKeyStoreRepositoryExtensionImpl : RsaKeyStoreRepositoryExtension {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findLatestOneYoungerThan(timeLimit: LocalDateTime) = Optional.ofNullable(
            em.createQuery("""
                SELECT kp
                FROM RsaKeyPair kp
                WHERE kp.isEnabled = true
                    AND kp.issuedAt >= :timeLimit
                ORDER BY kp.issuedAt DESC
            """.trimIndent(), RsaKeyPair::class.java)
                    .setParameter("timeLimit", timeLimit)
                    .setMaxResults(1)
                    .singleResult)
}
