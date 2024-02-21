package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
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

    private final BinanceDataLoaderService binanceDataLoaderService;

    public void execute() {
        log.debug("Load [{}] data from binance...", TimeFrameEnum.FIFTEEN_MINUTES);
        binanceDataLoaderService.updateAllSymbols(TimeFrameEnum.FIFTEEN_MINUTES);
    }
}