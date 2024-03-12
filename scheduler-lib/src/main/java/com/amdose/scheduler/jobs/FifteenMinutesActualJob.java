package com.amdose.scheduler.jobs;

import com.amdose.scheduler.exposed.IFifteenMinutesJob;
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
public class FifteenMinutesActualJob implements Job {

    @Autowired(required = false)
    private List<IFifteenMinutesJob> fifteenMinutesJobList;

    public void execute(JobExecutionContext context) {

        if (fifteenMinutesJobList == null || fifteenMinutesJobList.size() == 0) {
            return;
        }

        for (IFifteenMinutesJob job : fifteenMinutesJobList) {
            try {
                job.execute();
            } catch (Exception e) {
                log.error("Error occurred when executing: [{}]", job.getClass(), e);
            }
        }
    }
}