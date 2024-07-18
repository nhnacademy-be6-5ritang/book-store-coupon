package com.nhnacademy.bookstorecoupon.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Coupon API").version("1.0").description("쿠폰 API 명세서"));
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/coupons/**"};
        String[] packagesToScan = {"com.nhnacademy.bookstorecoupon"};
        return GroupedOpenApi.builder()
                .group("coupon-api")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}


