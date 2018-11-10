package com.github.fj.restapi.persistence.repository

import com.github.fj.restapi.persistence.entity.AccessLog
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Nov - 2018
 */
interface AccessLogRepository : JpaRepository<AccessLog, Long>
