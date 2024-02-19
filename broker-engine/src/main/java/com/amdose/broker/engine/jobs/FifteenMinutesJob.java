package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FifteenMinutesJob extends BaseJob {

    private final BinanceDataLoaderService binanceDataLoaderService;

    @Override
    TimeFrameEnum getInterval() {
        return TimeFrameEnum.FIFTEEN_MINUTES;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("Load [{}] data from binance...", this.getInterval());
        binanceDataLoaderService.updateAllSymbols(this.getInterval());
    }
}