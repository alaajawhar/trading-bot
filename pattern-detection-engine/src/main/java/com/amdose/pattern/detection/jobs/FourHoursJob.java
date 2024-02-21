package com.amdose.pattern.detection.jobs;

import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.pattern.detection.services.IndicatorsService;
import com.amdose.scheduler.exposed.IFourHoursJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FourHoursJob implements IFourHoursJob {

    private final IndicatorsService indicatorsService;

    @Override
    public void execute() {
        log.debug("Run indicators on [{}] data...", TimeFrameEnum.FOUR_HOURS);
        indicatorsService.runAllIndicators(TimeFrameEnum.FOUR_HOURS);
    }
}