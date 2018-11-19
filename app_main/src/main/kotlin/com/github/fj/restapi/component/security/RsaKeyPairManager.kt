/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.security

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
interface RsaKeyPairManager {
    fun getLatest(): RsaKeyPairEntry

    fun getById(id: Long): RsaKeyPairEntry
}
