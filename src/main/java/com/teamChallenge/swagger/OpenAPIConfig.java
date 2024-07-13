package com.teamChallenge.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        name = "BearerAuth")

public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi apiLinksV1() {
        return GroupedOpenApi.builder()
                .group("Figures API V1")
                .pathsToMatch("/api/figures/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiAuth() {
        return GroupedOpenApi.builder()
                .group("Users-Auth API")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    @Primary
    public OpenAPI customOpenAPIv1() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Store API")
                        .version("v1")
                        .description("API Documentation"));
    }

}
