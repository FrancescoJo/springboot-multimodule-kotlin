/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.repository

import com.github.fj.restapi.model.Member
import org.springframework.data.repository.CrudRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
interface MemberRepository : CrudRepository<Member, Long>
