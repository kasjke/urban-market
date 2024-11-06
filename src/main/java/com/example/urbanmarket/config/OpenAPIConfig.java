package com.example.urbanmarket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
/*
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        name = "BearerAuth")
 */
public class OpenAPIConfig {
    /*
    @Bean
    public GroupedOpenApi apiAuth() {
        return GroupedOpenApi.builder()
                .group("Users-Auth API")
                .pathsToMatch("/auth/**")
                .build();
    }
     */
    @Bean
    public GroupedOpenApi apiCartsV1() {
        return GroupedOpenApi.builder()
                .group("Carts API V1")
                .pathsToMatch("/api/v1/carts/**")
                .build();
    }
    @Bean
    public GroupedOpenApi apiOrdersV1() {
        return GroupedOpenApi.builder()
                .group("Orders API V1")
                .pathsToMatch("/api/v1/orders/**")
                .build();
    }
    @Bean
    public GroupedOpenApi apiProductsV1() {
        return GroupedOpenApi.builder()
                .group("Products API V1")
                .pathsToMatch("/api/v1/products/**")
                .build();
    }
    @Bean
    public GroupedOpenApi apiShopsV1() {
        return GroupedOpenApi.builder()
                .group("Shops API V1")
                .pathsToMatch("/api/v1/shops/**")
                .build();
    }
    @Bean
    public GroupedOpenApi apiUsersV1() {
        return GroupedOpenApi.builder()
                .group("Users API V1")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }
    @Bean
    @Primary
    public OpenAPI customOpenAPIv1() {
        return new OpenAPI()
                .info(new Info()
                        .title("Urban Zen API")
                        .version("v1")
                        .description("API Documentation"));
    }
}