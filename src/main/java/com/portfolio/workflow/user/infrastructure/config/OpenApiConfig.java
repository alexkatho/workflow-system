package com.portfolio.workflow.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workflowApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Workflow Management API")
                        .description("Backend API for workflow approval system")
                        .version("1.0.0"));
    }
}