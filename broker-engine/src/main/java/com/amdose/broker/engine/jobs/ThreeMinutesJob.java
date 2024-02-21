package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
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

    private final BinanceDataLoaderService binanceDataLoaderService;

    @Override
    public void execute() {
        log.debug("Load [{}] data from binance...", TimeFrameEnum.THREE_MINUTES);
        binanceDataLoaderService.updateAllSymbols(TimeFrameEnum.THREE_MINUTES);
    }
}