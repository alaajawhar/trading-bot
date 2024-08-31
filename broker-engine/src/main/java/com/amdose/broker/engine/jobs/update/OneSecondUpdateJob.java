package com.amdose.broker.engine.jobs.update;

import com.amdose.broker.engine.services.DataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.scheduler.exposed.IOneSecondsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alaa Jawhar
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class OneSecondUpdateJob implements IOneSecondsJob {

    private final DataLoaderService dataLoaderService;

    @Override
    public void execute() {
        log.info("Fetching [{}] data online...", TimeFrameEnum.ONE_SECOND);
        dataLoaderService.updateAllSymbols(TimeFrameEnum.ONE_SECOND);
    }
}