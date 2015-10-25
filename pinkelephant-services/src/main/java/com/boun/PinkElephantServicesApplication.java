package com.boun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.boun.*")
public class PinkElephantServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinkElephantServicesApplication.class, args);
    }
}
