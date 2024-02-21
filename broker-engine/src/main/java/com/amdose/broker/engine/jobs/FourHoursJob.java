package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
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

    private final BinanceDataLoaderService binanceDataLoaderService;

    @Override
    public void execute() {
        log.debug("Load [{}] data from binance...", TimeFrameEnum.FOUR_HOURS);
        binanceDataLoaderService.updateAllSymbols(TimeFrameEnum.FIFTEEN_MINUTES);
    }
}