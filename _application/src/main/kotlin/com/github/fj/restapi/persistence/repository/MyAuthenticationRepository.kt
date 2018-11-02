/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.repository

import com.github.fj.restapi.persistence.entity.MyAuthentication
import org.springframework.data.repository.CrudRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2018
 */
interface MyAuthenticationRepository : CrudRepository<MyAuthentication, Long>
