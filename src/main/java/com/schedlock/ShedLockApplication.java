package com.schedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShedLockApplication.class, args);
    }

}
