package com.metamorfose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Classe principal da aplicação Spring Boot
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class MetamorfoseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetamorfoseApplication.class, args);

        System.out.println("🌿 Metamorfose API iniciada com sucesso!");
        System.out.println("📖 Documentação Swagger: http://localhost:8080/api/v1/swagger-ui.html");
        System.out.println("🔧 API Base URL: http://localhost:8080/api/v1");
    }
}
