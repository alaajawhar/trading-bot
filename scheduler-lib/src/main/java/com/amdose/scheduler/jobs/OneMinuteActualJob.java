package com.amdose.scheduler.jobs;

import com.amdose.scheduler.exposed.IOneMinuteJob;
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
public class OneMinuteActualJob implements Job {

    @Autowired(required = false)
    private List<IOneMinuteJob> oneMinuteJobList;

    public void execute(JobExecutionContext context) {
        if (oneMinuteJobList == null || oneMinuteJobList.size() == 0) {
            return;
        }

        for (IOneMinuteJob job : oneMinuteJobList) {
            try {
                job.execute();
            } catch (Exception e) {
                log.error("Error occurred when executing: [{}]", job.getClass(), e);
            }
        }
    }
}