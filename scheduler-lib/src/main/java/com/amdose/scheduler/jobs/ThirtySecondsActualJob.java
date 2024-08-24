package com.amdose.scheduler.jobs;

import com.amdose.scheduler.exposed.BaseJob;
import com.amdose.scheduler.exposed.IThirtySecondsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThirtySecondsActualJob implements Job {

    @Autowired(required = false)
    private List<IThirtySecondsJob> thirtySecondsJobs;

    public void execute(JobExecutionContext context) {
        if (thirtySecondsJobs == null || thirtySecondsJobs.size() == 0) {
            return;
        }

        for (BaseJob job : thirtySecondsJobs) {
            try {
                job.execute();
            } catch (Exception e) {
                log.error("Error occurred when executing: [{}]", job.getClass(), e);
            }
        }
    }
}