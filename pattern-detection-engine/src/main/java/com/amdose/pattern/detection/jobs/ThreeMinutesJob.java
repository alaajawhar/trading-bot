package com.amdose.pattern.detection.jobs;

import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.pattern.detection.services.IndicatorsService;
import com.amdose.scheduler.exposed.IThreeMinutesJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThreeMinutesJob implements IThreeMinutesJob {

    private final IndicatorsService indicatorsService;

    public void execute() {
        log.debug("Run indicators on [{}] data...", TimeFrameEnum.THREE_MINUTES);
        indicatorsService.runAllIndicators(TimeFrameEnum.THREE_MINUTES);
    }
}