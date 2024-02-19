package com.amdose.broker.engine.config;

import com.amdose.broker.engine.jobs.*;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alaa Jawhar
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail oneMinuteJobDetails() {
        return JobBuilder.newJob(OneMinuteJob.class)
                .withIdentity("oneMinuteJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneMinuteJobTrigger(JobDetail oneMinuteJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(oneMinuteJobDetails)
                .withIdentity("oneMinuteTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
                .build();
    }

    @Bean
    public JobDetail threeMinutesJobDetails() {
        return JobBuilder.newJob(ThreeMinutesJob.class)
                .withIdentity("threeMinutesJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger threeMinutesJobTrigger(JobDetail threeMinutesJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(threeMinutesJobDetails)
                .withIdentity("threeMinutesTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(3))
                .build();
    }

    @Bean
    public JobDetail fifteenMinutesJobDetails() {
        return JobBuilder.newJob(FifteenMinutesJob.class)
                .withIdentity("fifteenMinutesJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fifteenMinutesJobTrigger(JobDetail fifteenMinutesJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(fifteenMinutesJobDetails)
                .withIdentity("fifteenMinutesTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(15))
                .build();
    }

    @Bean
    public JobDetail oneHourJobDetails() {
        return JobBuilder.newJob(OneHourJob.class)
                .withIdentity("oneHourJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneHourJobTrigger(JobDetail oneHourJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(oneHourJobDetails)
                .withIdentity("oneHourTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(1))
                .build();
    }

    @Bean
    public JobDetail fourHoursJobDetails() {
        return JobBuilder.newJob(FourHoursJob.class)
                .withIdentity("fourHoursJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fourHoursJobTrigger(JobDetail fourHoursJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(fourHoursJobDetails)
                .withIdentity("fourHoursTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(4))
                .build();
    }

    @Bean
    public JobDetail oneDayJobDetails() {
        return JobBuilder.newJob(OneDayJob.class)
                .withIdentity("oneDayJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneDayJobTrigger(JobDetail oneDayJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(oneDayJobDetails)
                .withIdentity("oneDayTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24))
                .build();
    }
}
