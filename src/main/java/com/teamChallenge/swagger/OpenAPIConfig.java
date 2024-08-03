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
    public GroupedOpenApi apiAdsV1() {
        return GroupedOpenApi.builder()
                .group("Ads API V1")
                .pathsToMatch("/api/ads/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiFiguresV1() {
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
    public GroupedOpenApi apiCartsV1() {
        return GroupedOpenApi.builder()
                .group("Carts API V1")
                .pathsToMatch("/api/carts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiOrdersV1() {
        return GroupedOpenApi.builder()
                .group("Orders API V1")
                .pathsToMatch("/api/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiUsersV1() {
        return GroupedOpenApi.builder()
                .group("Users API V1")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiCategoriesV1() {
        return GroupedOpenApi.builder()
                .group("Categories API V1")
                .pathsToMatch("/api/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiSubCategoriesV1() {
        return GroupedOpenApi.builder()
                .group("SubCategories API V1")
                .pathsToMatch("/api/subCategories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiBannersV1() {
        return GroupedOpenApi.builder()
                .group("Banners API V1")
                .pathsToMatch("/api/banners/**")
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