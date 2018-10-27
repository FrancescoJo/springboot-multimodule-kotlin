/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.endpoint.ApiPaths
import com.google.common.base.Predicate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Oct - 2018
 */
@AllOpen
@Configuration
@EnableSwagger2
class SwaggerConfig : WebMvcConfigurer {
    /*
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */
    // For multiple Docket configuration see https://github.com/springfox/springfox/issues/748
    @Bean
    fun hello(): Docket =
            newDocket("Hello world",
                    "/${ApiPaths.HELLO}", "Collection of hello world APIs.",
                    PathSelectors.ant("/${ApiPaths.HELLO}"))

    @Bean
    fun account(): Docket =
            newDocket("Account",
                    "/${ApiPaths.ACCOUNT}", "Collection of account related APIs.",
                    PathSelectors.ant("/${ApiPaths.ACCOUNT}"))

    @Bean
    fun api(): Docket =
            newDocket("API: ${ApiPaths.V1}",
                    "/${ApiPaths.API_V1}", "Collection of ${ApiPaths.V1} APIs.",
                    PathSelectors.ant("/${ApiPaths.API_V1}/**"))

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    companion object {
        private fun newDocket(group: String, title: String, description: String,
                              pathPredicate: Predicate<String>) = Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(group)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(pathPredicate)
                .build()
                .apiInfo(newApiInfo(title, description))

        private fun newApiInfo(title: String, description: String) = ApiInfo(
                title,
                description,
                ApiPaths.API_V1,
                "Terms of service",
                Contact("Francesco Jo",
                        "https://github.com/FrancescoJo/springboot-multimodule-kotlin",
                        "nimbusob@gmail.com"
                ),
                "Distributed under no licences and warranty.",
                "https://tldrlegal.com/license/no-limit-public-license",
                Collections.emptyList()
        )
    }
}
