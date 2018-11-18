/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.component.auth

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.AppProperties
import com.github.fj.restapi.component.auth.TokenGenerationMethod.INHOUSE
import com.github.fj.restapi.component.auth.inhouse.InhouseAccessTokenBusinessImpl
import com.github.fj.restapi.component.auth.jwt.JwtAccessTokenBusinessImpl
import com.github.fj.restapi.persistence.repository.UserRepository
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2018
 */
@AllOpen
@Component
class AccessTokenBusinessFactory @Inject constructor(
        private val appProperties: AppProperties,
        private val userRepository: UserRepository
) {
    private val inhouseAuthBusiness: AccessTokenBusiness by lazy {
        return@lazy InhouseAccessTokenBusinessImpl(appProperties, userRepository)
    }

    private val jwtAuthBusiness: AccessTokenBusiness by lazy {
        JwtAccessTokenBusinessImpl()
    }

    fun get(): AccessTokenBusiness {
        return when (appProperties.tokenGenerationMethod) {
            INHOUSE -> inhouseAuthBusiness
            else -> jwtAuthBusiness
        }
    }
}
