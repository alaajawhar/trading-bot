package com.amdose.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.amdose", "io.swagger"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
