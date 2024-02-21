package com.amdose.broker.engine;

import com.amdose.database.DatabaseModule;
import com.amdose.scheduler.SchedulerModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@Import({DatabaseModule.class, SchedulerModule.class})
public class BrokerModule {

    public static void main(String[] args) {
        SpringApplication.run(BrokerModule.class, args);
    }
}
