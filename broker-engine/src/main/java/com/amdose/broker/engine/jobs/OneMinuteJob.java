package com.amdose.broker.engine.jobs;

import com.amdose.broker.engine.services.BinanceDataLoaderService;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.ICandleRepository;
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
public class OneMinuteJob extends BaseJob {

    private final BinanceDataLoaderService binanceDataLoaderService;
    private final ICandleRepository candleRepository;


    @Override
    TimeFrameEnum getInterval() {
        return TimeFrameEnum.ONE_MINUTE;
    }

    @Override
    public void execute(JobExecutionContext context) {
//        candleRepository.deleteAll(); // TODO: remove
        log.debug("Load [{}] data from binance...", this.getInterval());
        binanceDataLoaderService.updateAllSymbols(this.getInterval());
    }
}