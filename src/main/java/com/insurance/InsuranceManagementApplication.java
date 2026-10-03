package com.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InsuranceManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsuranceManagementApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Insurance Policy Management System Started ");
        System.out.println("  Access: http://localhost:8082              ");
        System.out.println("  Login:  admin / admin                      ");
        System.out.println("==============================================");
    }
}
