package com.shoppiee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.shoppiee.model")
@EnableJpaRepositories(basePackages = "com.shoppiee.repository")
public class ShoppieeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppieeApplication.class, args);
    }
}
