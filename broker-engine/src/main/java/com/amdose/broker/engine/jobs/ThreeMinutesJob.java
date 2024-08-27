package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.DataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
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

    private final DataLoaderService dataLoaderService;

    @Override
    public void execute() {
        log.debug("Fetching [{}] data online...", TimeFrameEnum.THREE_MINUTES);
        dataLoaderService.updateAllSymbols(TimeFrameEnum.THREE_MINUTES);
    }
}