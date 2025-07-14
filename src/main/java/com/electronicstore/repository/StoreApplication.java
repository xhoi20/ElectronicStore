package com.electronicstore.repository;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.electronicstore.entity")
@EnableJpaRepositories(basePackages = {"com.electronicstore.repository"})
@ComponentScan(basePackages = {"com.electronicstore.controller","com.electronicstore.service","com.electronicstore.tokenlogin"})
@EnableEncryptableProperties

public class StoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}