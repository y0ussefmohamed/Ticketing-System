package com.projects.Ticketing.System.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean // Documentation Title Details
    public OpenAPI inventoryServiceApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("Inventory Service API for the Ticketing System")
                        .version("v1.0.0"));
    }
}
