package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.scheduler.exposed.IOneMinuteJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OneMinuteJob implements IOneMinuteJob {

    private final BinanceDataLoaderService binanceDataLoaderService;

    @Override
    public void execute() {
        log.debug("Load [{}] data from binance...", TimeFrameEnum.ONE_MINUTE);
        binanceDataLoaderService.updateAllSymbols(TimeFrameEnum.ONE_MINUTE);
    }
}