package com.amdose.scheduler.jobs;

import com.amdose.scheduler.exposed.BaseJob;
import com.amdose.scheduler.exposed.IOneHourJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OneHourActualJob implements Job {

    @Autowired(required = false)
    private List<IOneHourJob> oneHourJobList;

    public void execute(JobExecutionContext context) {

        if (oneHourJobList == null || oneHourJobList.size() == 0) {
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(oneHourJobList.size());

        for (BaseJob job : oneHourJobList) {
            executorService.submit(() -> {
                try {
                    job.execute();
                } catch (Exception e) {
                    log.error("Error occurred when executing: [{}]", job.getClass(), e);
                }
            });
        }

        executorService.shutdown();
    }
}