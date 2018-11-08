/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.aop

import com.github.fj.restapi.persistence.consts.UserActivity

/**
 * Denotes that annotated method should be logged by
 * [com.github.fj.restapi.appconfig.aop.internal.EndpointAccessLogAspect].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggedActivity(val activity: UserActivity)
