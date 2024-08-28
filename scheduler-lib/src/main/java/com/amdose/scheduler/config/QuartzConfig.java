package com.amdose.scheduler.config;

import com.amdose.scheduler.jobs.*;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    @Bean
    public JobDetail oneSecondJobDetails() {
        return JobBuilder.newJob(OneSecondsActualJob.class)
                .withIdentity("oneSecondsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneSecondsJobTrigger(JobDetail oneSecondJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 1);

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(oneSecondJobDetails)
                .withIdentity("oneSecondsJob")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))
                .build();
    }

    @Bean
    public JobDetail oneMinuteJobDetails() {
        return JobBuilder.newJob(OneMinuteActualJob.class)
                .withIdentity("oneMinuteJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneMinuteJobTrigger(JobDetail oneMinuteJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 1); // Move to the next minute

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(oneMinuteJobDetails)
                .withIdentity("oneMinuteTrigger")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
                .build();
    }

    @Bean
    public JobDetail threeMinutesJobDetails() {
        return JobBuilder.newJob(ThreeMinutesActualJob.class)
                .withIdentity("threeMinutesJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger threeMinutesJobTrigger(JobDetail threeMinutesJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR, 1);

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(threeMinutesJobDetails)
                .startAt(startTime)
                .withIdentity("threeMinutesTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(3))
                .build();
    }

    @Bean
    public JobDetail fifteenMinutesJobDetails() {
        return JobBuilder.newJob(FifteenMinutesActualJob.class)
                .withIdentity("fifteenMinutesJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fifteenMinutesJobTrigger(JobDetail fifteenMinutesJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR, 1);

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(fifteenMinutesJobDetails)
                .startAt(startTime)
                .withIdentity("fifteenMinutesTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(15))
                .build();
    }

    @Bean
    public JobDetail oneHourJobDetails() {
        return JobBuilder.newJob(OneHourActualJob.class)
                .withIdentity("oneHourJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneHourJobTrigger(JobDetail oneHourJobDetails) {

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR, 1); // Move to the next minute

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(oneHourJobDetails)
                .startAt(startTime)
                .withIdentity("oneHourTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(1))
                .build();
    }

    @Bean
    public JobDetail fourHoursJobDetails() {
        return JobBuilder.newJob(FourHoursActualJob.class)
                .withIdentity("fourHoursJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fourHoursJobTrigger(JobDetail fourHoursJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR, 24); // Move to the next minute

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(fourHoursJobDetails)
                .startAt(startTime)
                .withIdentity("fourHoursTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(4))
                .build();
    }

    @Bean
    public JobDetail oneDayJobDetails() {
        return JobBuilder.newJob(OneDayActualJob.class)
                .withIdentity("oneDayJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger oneDayJobTrigger(JobDetail oneDayJobDetails) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.HOUR, 24); // Move to the next day

        Date startTime = calendar.getTime();

        return TriggerBuilder.newTrigger()
                .forJob(oneDayJobDetails)
                .withIdentity("oneDayTrigger")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24))
                .build();
    }
}
