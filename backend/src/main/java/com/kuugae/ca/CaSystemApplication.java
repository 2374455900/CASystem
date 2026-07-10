package com.kuugae.ca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaSystemApplication.class, args);
    }
}
