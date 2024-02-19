package com.amdose.pattern.detection.jobs;

import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.pattern.detection.services.IndicatorsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OneDayJob extends BaseJob {

    private final IndicatorsService indicatorsService;

    @Override
    TimeFrameEnum getInterval() {
        return TimeFrameEnum.ONE_DAY;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("Run indicators on [{}] data...", this.getInterval());
        indicatorsService.runAllIndicators(this.getInterval());
    }
}