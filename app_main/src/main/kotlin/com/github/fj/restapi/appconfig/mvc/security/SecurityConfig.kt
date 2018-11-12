/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.appconfig.mvc.security.internal.*
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig @Inject constructor(
        private val authBusiness: AuthenticationBusiness,
        private val successHandler: SavedRequestAwareAuthenticationSuccessHandler,
        private val failureHandler: AuthenticationFailureHandler
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(HttpAuthorizationTokenAuthenticationProvider(LOG, authBusiness))
    }

    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(HttpServletRequestAuthorizationHeaderFilter(LOG, FILTER_EXCLUDE_REQUESTS.toList()),
                BasicAuthenticationFilter::class.java)
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

    companion object {
        private val FILTER_EXCLUDE_REQUESTS = arrayOf(
                AntPathRequestMatcher(ApiPaths.API_V1_ACCOUNT, HttpMethod.POST.toString()),
                AntPathRequestMatcher(ApiPaths.API_V1_ACCOUNT, HttpMethod.PATCH.toString()),
                AntPathRequestMatcher(ApiPaths.ERROR)
        )

        private val LOG = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}
