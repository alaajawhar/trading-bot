package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.DataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.scheduler.exposed.IFifteenMinutesJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FifteenMinutesJob implements IFifteenMinutesJob {

    private final DataLoaderService dataLoaderService;

    public void execute() {
        log.debug("Fetching [{}] data online...", TimeFrameEnum.FIFTEEN_MINUTES);
        dataLoaderService.updateAllSymbols(TimeFrameEnum.FIFTEEN_MINUTES);
    }
}