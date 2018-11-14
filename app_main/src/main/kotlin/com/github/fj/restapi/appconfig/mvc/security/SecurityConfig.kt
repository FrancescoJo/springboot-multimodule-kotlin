/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.mvc.security.internal.AuthenticationEntryPointImpl
import com.github.fj.restapi.appconfig.mvc.security.internal.AuthenticationFailureHandler
import com.github.fj.restapi.appconfig.mvc.security.internal.HttpAuthorizationTokenAuthenticationProvider
import com.github.fj.restapi.appconfig.mvc.security.internal.HttpServletRequestAuthorizationHeaderFilter
import com.github.fj.restapi.appconfig.mvc.security.internal.SavedRequestAwareAuthenticationSuccessHandler
import com.github.fj.restapi.appconfig.mvc.security.spel.PreAuthorizeSpelInterceptor
import com.github.fj.restapi.component.account.AuthenticationBusiness
import com.github.fj.restapi.endpoint.ApiPaths
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Configuration
@EnableWebSecurity
class SecurityConfig @Inject constructor(
        private val authBusiness: AuthenticationBusiness,
        private val successHandler: SavedRequestAwareAuthenticationSuccessHandler,
        private val failureHandler: AuthenticationFailureHandler
) : WebSecurityConfigurerAdapter(), WebMvcConfigurer {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(HttpAuthorizationTokenAuthenticationProvider(LOG, authBusiness))
    }

    override fun configure(http: HttpSecurity) {
        @Suppress("SpreadOperator") // This logic is called only once.
        http.addFilterBefore(HttpServletRequestAuthorizationHeaderFilter(LOG,
                FILTER_EXCLUDE_REQUESTS.toList()), BasicAuthenticationFilter::class.java)
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(AuthenticationEntryPointImpl())
                .and()
                .authorizeRequests()
                // Restrict Spring actuator access except from localhost for security
                .antMatchers("/actuator/**").hasIpAddress("localhost")
                .requestMatchers(*FILTER_EXCLUDE_REQUESTS).permitAll()
                .antMatchers("${ApiPaths.API}/**").authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .deleteCookies()
                .invalidateHttpSession(true)
    }

    /**
     * As [org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration]
     * noted, this configuration is must be provided to bypass Spring security default configuration.
     */
    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(tokenAuthProvider()))
    }

    @Bean
    fun tokenAuthProvider(): AuthenticationProvider =
            HttpAuthorizationTokenAuthenticationProvider(LOG, authBusiness)

    /**
     * A reinvented wheel version of [org.springframework.security.access.expression.method.MethodSecurityExpressionRoot]
     * inspected by [org.springframework.security.web.access.intercept.FilterSecurityInterceptor],
     * which is used by Spring security by contract, enabled by `@EnableGlobalMethodSecurity(prePostEnabled = true)`.
     * Thus, it will not work if `@EnableGlobalMethodSecurity` is applied. The side effect is
     * explained below:
     *
     * We tried so hard to find a way to pass our custom authentication object to security filter.
     * However, when the request hits a method annotated either `@Pre/Post Authorize`, the our
     * 'should be' authentication object is not passed into it. Moreover, the exception messages
     * are difficult to decode because there is no `javax.servlet.error.exception` context
     * inside the `HttpServletRequest`, when the exception occur by Servlet security failure.
     *
     * Actually, we can use the role, authority and/or whatever authorisation if we place a proper
     * setting to [org.springframework.security.config.annotation.web.builders.HttpSecurity] object.
     * However, that approach does not seem to be good, because we have to write the settings into
     * both places - add a setup first, and write annotation at the business logic method second.
     * Although it's inevitable for goods, but duplicated and scattered intention is not a desirable
     * design.
     *
     * This happens because the Spring security heavily relies on Servlet filter that running context
     * is completely separated from ApplicationContext. Hope there is an improvement to achieve security
     * feature more easily just within the Spring world, not in a conjunction with Servlet.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(PreAuthorizeSpelInterceptor(applicationContext))
    }

    companion object {
        private val FILTER_EXCLUDE_REQUESTS = arrayOf(
                AntPathRequestMatcher(ApiPaths.API_V1_ACCOUNT, HttpMethod.POST.toString()),
                AntPathRequestMatcher(ApiPaths.API_V1_ACCOUNT, HttpMethod.PATCH.toString()),
                AntPathRequestMatcher(ApiPaths.ERROR)
        )

        private val LOG = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}
