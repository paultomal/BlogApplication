package com.example.nafiz.blog.common;


import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blog API")
                        .version("1.0")
                        .description("Blog Management REST API using Spring Boot")
                        .contact(new Contact()
                                .name("Nafiz")
                                .email("nafiz@example.com")
                        )
                );
    }
}
