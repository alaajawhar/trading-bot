package com.amdose.base.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;


/**
 * @author Alaa Jawhar
 */
@Slf4j
@Configuration
public class AppConfiguration {

    @PostConstruct
    public void init() {
        System.out.println("Initializing Application Context....");
    }
}
