package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.DataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.scheduler.exposed.IOneDayJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OneDayJob implements IOneDayJob {

    private final DataLoaderService dataLoaderService;

    @Override
    public void execute() {
        log.debug("Fetching [{}] data online...", TimeFrameEnum.ONE_DAY);
        dataLoaderService.updateAllSymbols(TimeFrameEnum.ONE_DAY);
    }
}