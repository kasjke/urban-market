package com.example.urbanmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class UrbanMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanMarketApplication.class, args);
    }

}
