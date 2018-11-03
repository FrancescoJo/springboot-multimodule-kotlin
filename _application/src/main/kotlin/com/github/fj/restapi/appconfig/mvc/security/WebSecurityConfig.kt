/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig.mvc.security

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.endpoint.ApiPaths
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@AllOpen
@Configuration
@EnableWebSecurity
class WebSecurityConfig @Inject constructor(
        private val authEntryPoint: AuthenticationEntryPointImpl,
        private val successHandler: SavedRequestAwareAuthenticationSuccessHandler,
        private val failureHandler: AuthenticationFailureHandler
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        // TODO: auth.authenticationProvider()
        // TODO: constanise roles
        auth.inMemoryAuthentication()
                .withUser("admin").password("adminPass").roles("ADMIN")
                .and()
                .withUser("user").password("userPass").roles("USER")
    }

    override fun configure(http: HttpSecurity) {
        http.cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .authorizeRequests()
                // Restrict Spring actuator access except from localhost for security
                .antMatchers("/actuator/**").hasIpAddress("localhost")
                .antMatchers(HttpMethod.POST, ApiPaths.API_V1_ACCOUNT).permitAll()
                .antMatchers(HttpMethod.PATCH, ApiPaths.API_V1_ACCOUNT).hasAnyRole("USER")
                .antMatchers(HttpMethod.DELETE, ApiPaths.API_V1_ACCOUNT).hasAnyRole("USER")
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .deleteCookies()
                .invalidateHttpSession(true)
    }

    // TODO: disable userDetailsManager autoconf
}
