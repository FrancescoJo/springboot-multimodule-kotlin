/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository

import com.github.fj.restapi.persistence.entity.RsaKeyPair
import com.github.fj.restapi.persistence.repository.extended.RsaKeyStoreRepositoryExtension
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
interface RsaKeyStoreRepository : JpaRepository<RsaKeyPair, String>, RsaKeyStoreRepositoryExtension
