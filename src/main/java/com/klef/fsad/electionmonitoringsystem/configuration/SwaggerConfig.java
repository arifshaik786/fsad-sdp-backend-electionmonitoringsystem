package com.klef.fsad.electionmonitoringsystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig
{
    @Bean
    public OpenAPI apiInfo()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Election Monitoring System")
                        .version("1.0")
                        .description("API for Election Monitoring System"));
    }
}