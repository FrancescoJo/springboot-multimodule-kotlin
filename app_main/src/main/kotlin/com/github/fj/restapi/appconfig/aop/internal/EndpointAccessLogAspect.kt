/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.aop.internal

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.aop.LoggedActivity
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.UserActivity
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

/**
 * Determines given [org.aspectj.lang.JoinPoint] has [com.github.fj.restapi.appconfig.aop.LoggedActivity]
 * as its annotation. If the annotation is found, logs all inputs annotated with
 * [org.springframework.web.bind.annotation.RequestBody] and all outputs annotated with
 * [org.springframework.web.bind.annotation.ResponseBody] for tracking every user activity.
 *
 * Further customisations could be applied if this method logs everything out too much,
 * prints sensitive information, considered as a performance bottleneck, and/or causes operational
 * trouble.
 *
 * For example, considering introduction of log level parameters in
 * [com.github.fj.restapi.appconfig.aop.LoggedActivity] could be a quick and naïve fix.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-endpoints">Spring boot Actuator: <code>/httptrace</code> </a>
 */
@AllOpen
@Aspect
class EndpointAccessLogAspect {
    @Before("within(com.github.fj.restapi.endpoint.${ApiPaths.CURRENT_VERSION}..*)")
    fun logBeforeEndpoint(p: JoinPoint) {
        val activity = p.findLoggedActivity() ?: return
        println("LOGGING ACTIVITY: " + activity)

        // 2. is method has @RequestBody?

        // 3. is method has @ResponseBody?

        // p.getAllInputs() // Annotated as @RequestBody : just call toString

        // p.getAllOutputs() // Annotated as @ResponseBody : just call toString
    }

    @AfterReturning("within(com.github.fj.restapi.endpoint.${ApiPaths.CURRENT_VERSION}..*)", returning = "returned")
    fun logAfterEndpoint(p: JoinPoint, returned: Any?) {
        println("AFTER ENDPOINT: " + p)
    }

    @AfterThrowing("within(your.package.where.is.endpoint.${ApiPaths.CURRENT_VERSION}..*)", throwing = "e")
    fun logAfterException(p: JoinPoint, e: Exception?) {
        println("AFTER THROWING: " + p)
    }

    private fun JoinPoint.findLoggedActivity(): UserActivity? {
        val method = (signature as? MethodSignature)?.method ?: return null
        val myParams = method.parameters.map { it.type }.toTypedArray()
        val target = LoggedActivity::class.java

        val realMethod = findAnnotatedMethod(method, myParams, target) ?: return null
        return realMethod.getAnnotation(target).activity
    }

    private fun findAnnotatedMethod(m: Method, mArgTyps: Array<Class<*>>?,
                                    ak: Class<out Annotation>): Method? {
        if (m.annotations.any { it.annotationClass.qualifiedName == ak.canonicalName }) {
            return m
        }

        findAnnotatedMethod0(m.declaringClass.superclass, m, mArgTyps, ak)?.let { return it }

        m.declaringClass.interfaces.forEach { ifce ->
            findAnnotatedMethod0(ifce, m, mArgTyps, ak)?.let { return it }
        }

        return null
    }

    private fun findAnnotatedMethod0(superType: Class<*>?, m: Method, myArgTypes: Array<Class<*>>?,
                                     target: Class<out Annotation>): Method? =
            try {
                superType?.run {
                    return@run if (!myArgTypes.isNullOrEmpty()) {
                        getMethod(m.name, *myArgTypes)
                    } else {
                        getMethod(m.name)
                    }
                }
            } catch (e: ReflectiveOperationException) {
                null
            }?.let {
                findAnnotatedMethod(it, myArgTypes, target)
            }
}
