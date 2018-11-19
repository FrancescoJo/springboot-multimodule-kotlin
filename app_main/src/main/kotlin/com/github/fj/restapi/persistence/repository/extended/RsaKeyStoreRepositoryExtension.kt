/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository.extended

import com.github.fj.restapi.persistence.entity.RsaKeyPair
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
interface RsaKeyStoreRepositoryExtension {
    fun findLatestOneYoungerThan(timeLimit: LocalDateTime): Optional<RsaKeyPair>
}
