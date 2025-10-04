package com.metamorfose.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Metamorfose API")
                        .description("Sistema de Monitoramento de Plantas - API REST integrada com PL/SQL")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Metamorfose")
                                .email("contato@metamorfose.io@gmail.com")
                                .url("https://metamorfose.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}