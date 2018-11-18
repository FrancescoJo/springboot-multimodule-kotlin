/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.aop.internal

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.utcNow
import com.github.fj.restapi.appconfig.aop.LoggedActivity
import com.github.fj.restapi.component.auth.AuthenticationObjectImpl
import com.github.fj.restapi.endpoint.ApiPaths
import com.github.fj.restapi.persistence.consts.UserActivity
import com.github.fj.restapi.persistence.entity.AccessLog
import com.github.fj.restapi.persistence.repository.AccessLogRepository
import com.github.fj.restapi.util.extractInetAddress
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.net.InetAddress

/**
 * Determines given [org.aspectj.lang.JoinPoint] has [com.github.fj.restapi.appconfig.aop.LoggedActivity]
 * as its annotation. If the annotation is found, logs all inputs annotated with
 * [org.springframework.web.bind.annotation.RequestBody], [org.springframework.web.bind.annotation.RequestParam]
 * and all outputs annotated with [org.springframework.web.bind.annotation.ResponseBody]
 * for tracking every user activity.
 *
 * Further customisations could be applied if this method logs everything out too much,
 * prints sensitive information, considered as a performance bottleneck, and/or causes operational
 * trouble.
 *
 * For example, considering introduction of log level parameters in
 * [com.github.fj.restapi.appconfig.aop.LoggedActivity] could be a quick and naïve fix.
 *
 * Or, simply remove bean creation of this class.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-endpoints">Spring boot Actuator: <code>/httptrace</code> </a>
 */
@AllOpen
// @org.aspectj.lang.annotation.Aspect.Aspect   // Not preferrable on slower machines
class EndpointAccessLogAspect(private val logRepo: AccessLogRepository) {
    @Before("within(com.github.fj.restapi.endpoint.${ApiPaths.CURRENT_VERSION}..*)")
    fun logBeforeEndpoint(p: JoinPoint) {
        val activity = p.findLoggedActivity() ?: return
        val logObject = CURRENT_LOGGING_VALUE.get().apply {
            this.activity = activity
        }
        val method = (p.signature as MethodSignature).method
        val paramTypes = method.parameterTypes
        val noAnnotationParams = ArrayList<Parameter>()
        method.parameters.forEachIndexed { i, param ->
            if (param.hasLoggableAnnotation()) {
                logObject.inputs.add(p.args[i])
            } else {
                noAnnotationParams.add(param)
            }
        }

        var enclosingClass = with(method.declaringClass) {
            interfaces.forEach { ifce ->
                addLogAnnotatedParams(p, logObject, ifce, method.name, paramTypes)
            }
            return@with superclass
        }

        while (enclosingClass != null) {
            addLogAnnotatedParams(p, logObject, enclosingClass, method.name, paramTypes)
            enclosingClass = enclosingClass.superclass
        }
    }

    private fun addLogAnnotatedParams(p: JoinPoint, logObject: AccessLog, encasing: Class<*>,
                                      name: String, paramTypes: Array<Class<*>>) {
        try {
            val m = encasing.getMethod(name, *paramTypes)
            m.parameters.forEachIndexed { i, param ->
                if (param.hasLoggableAnnotation()) {
                    logObject.inputs.add(p.args[i])
                }
            }
        } catch (ignore: ReflectiveOperationException) {
        }
    }

    private fun Parameter.hasLoggableAnnotation(): Boolean = annotations.any {
        return@any it.annotationClass.qualifiedName.let { name ->
            RequestBody::class.qualifiedName == name || RequestParam::class.qualifiedName == name
        }
    }

    @AfterReturning("within(com.github.fj.restapi.endpoint.${ApiPaths.CURRENT_VERSION}..*)",
            returning = "returned")
    fun logAfterEndpoint(p: JoinPoint, returned: Any?) =
            logAfterInternal((p.signature as? MethodSignature)?.method, returned)

    @AfterThrowing("within(your.package.where.is.endpoint.${ApiPaths.CURRENT_VERSION}..*)",
            throwing = "e")
    fun logAfterException(p: JoinPoint, e: Exception) =
            logAfterInternal((p.signature as? MethodSignature)?.method, e)

    private fun logAfterInternal(m: Method?, logTarget: Any?) {
        val logObject = CURRENT_LOGGING_VALUE.get().takeIf {
            it.activity != UserActivity.UNDEFINED
        }

        if (m == null || logObject == null) {
            return
        }

        val myParams = m.parameters.map { it.type }.toTypedArray()
        val bodyKlass = ResponseBody::class
        val isResponseBody = if (findAnnotatedMethod(m, myParams, bodyKlass.java) != null) {
            true
        } else {
            m.returnType.annotations.any {
                bodyKlass.qualifiedName == it.annotationClass.qualifiedName
            }
        }

        logObject.run {
            timestamp = utcNow()
            userId = getCurrentUserId()
            ipAddr = currentUserKnownIP()

            input = inputs.joinToString("\n")
            output = if (isResponseBody) {
                if (logTarget is Exception) {
                    logTarget.message ?: logTarget.toString()
                } else {
                    logTarget.toString()
                }
            } else {
                ""
            }
            inputs.clear()
        }

        logRepo.save(logObject)
    }

    @VisibleForTesting
    protected fun getCurrentUserId(): Long =
            (SecurityContextHolder.getContext().authentication)?.let {
                return@let if (it is AuthenticationObjectImpl) {
                    it.details.id
                } else {
                    0L
                }
            } ?: 0L

    @VisibleForTesting
    protected fun currentUserKnownIP(): InetAddress =
            (RequestContextHolder.currentRequestAttributes() as? ServletRequestAttributes)
                    ?.request?.extractInetAddress() ?: InetAddressExtensions.EMPTY_INET_ADDRESS

    private fun JoinPoint.findLoggedActivity(): UserActivity? {
        val method = (signature as? MethodSignature)?.method ?: return null
        val myParams = method.parameters.map { it.type }.toTypedArray()
        val target = LoggedActivity::class.java

        return findAnnotatedMethod(method, myParams, target)
                ?.getAnnotation(target)
                ?.activity
    }

    @Suppress("ReturnCount")    // Early return is much readable in this case
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

    companion object {
        private val CURRENT_LOGGING_VALUE = object : ThreadLocal<AccessLog>() {
            override fun initialValue() = AccessLog()
        }
    }
}
