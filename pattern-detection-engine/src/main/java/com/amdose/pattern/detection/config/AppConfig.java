package com.amdose.pattern.detection.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author Alaa Jawhar
 */
@Slf4j
@Configuration
@EnableScheduling
public class AppConfig {

    @PostConstruct
    public void init() {
        System.out.println("Initializing Application Context....");
    }
}
