package com.ambravate.arcane.academy.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ambravate.arcane.academy")
public class ArcaneAcademyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArcaneAcademyApplication.class, args);
    }
}
