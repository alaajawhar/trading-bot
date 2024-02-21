package com.amdose.pattern.detection;

import com.amdose.database.DatabaseModule;
import com.amdose.scheduler.SchedulerModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@RequiredArgsConstructor
@Import({DatabaseModule.class, SchedulerModule.class})
@SpringBootApplication(scanBasePackages = {"com.amdose.pattern.detection"})
public class PatternDetectionModule {

    public static void main(String[] args) {
        SpringApplication.run(PatternDetectionModule.class, args);
    }
}
