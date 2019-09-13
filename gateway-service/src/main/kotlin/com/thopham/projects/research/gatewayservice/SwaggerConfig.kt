package com.thopham.projects.research.gatewayservice
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

@Configuration
@EnableSwagger2WebFlux
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.thopham.projects.research.gatewayservice.controllers"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        listOf(
                                ResponseMessageBuilder().code(500).message("INTERNAL_ERROR").build(),
                                ResponseMessageBuilder().code(400).message("BAD_REQUEST").build(),
                                ResponseMessageBuilder().code(404).message("NOT_FOUND").build()

                        )
                )
                .globalResponseMessage(RequestMethod.POST,
                        listOf(
                                ResponseMessageBuilder().code(500).message("INTERNAL_ERROR").build(),
                                ResponseMessageBuilder().code(400).message("BAD_REQUEST").build(),
                                ResponseMessageBuilder().code(404).message("NOT_FOUND").build()

                        )
                )
    }
}