/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.github.fj.lib.annotation.AllOpen
import com.github.fj.restapi.endpoint.ApiPaths
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
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(ApiPaths.VERSION_V1)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("(/hello)"))
                .build()
                .apiInfo(ApiInfo(
                        "REST API Demo",
                        "Collections of ${ApiPaths.VERSION_V1} hello world API.",
                        ApiPaths.BASE_PATH,
                        "Terms of service",
                        Contact("Francesco Jo",
                                "https://github.com/FrancescoJo/springboot-multimodule-kotlin",
                                "nimbusob@gmail.com"
                        ),
                        "Distributed under no licences and warranty.",
                        "https://tldrlegal.com/license/no-limit-public-license",
                        Collections.emptyList())
                )
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
