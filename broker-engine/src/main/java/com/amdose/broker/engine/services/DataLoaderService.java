package com.amdose.broker.engine.services;

import com.amdose.broker.engine.brokers.BinanceBroker;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.ISymbolRepository;
import com.amdose.utils.DateUtils;
import com.amdose.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final ICandleRepository candleRepository;
    private final BinanceBroker binanceBroker;
    private final ISymbolRepository symbolRepository;

    public void updateAllSymbols(TimeFrameEnum interval) {
        List<SymbolEntity> symbolList = symbolRepository.findAll();
        for (SymbolEntity symbolEntity : symbolList) {
            log.debug("update data of symbol: [{}] and [{}] interval", symbolEntity.getName(), interval);
            this.updateData(symbolEntity, interval);
        }
    }

    public void updateData(SymbolEntity symbol, TimeFrameEnum interval) {
        Optional<CandleEntity> lastCandle = candleRepository.findTopBySymbolAndTimeFrameOrderByDateDesc(symbol, interval);

        Date startDate = null;

        if (lastCandle.isPresent()) {
            startDate = lastCandle.get().getDate();
            startDate = interval.addTime(startDate); // to only retrieve 1 record instead of 2 which was already added
        }

        List<CandleEntity> candles = binanceBroker.getCandles(symbol, interval, startDate);

        if (startDate != null
                && candles.size() != 0
                && candles.get(candles.size() - 1).getDate().equals(interval.addTime(startDate))) {
            log.debug("Removing last candle: [{}] for startDate: [{}]", JsonUtils.convertToString(candles), DateUtils.convertToString(startDate));
            candles.remove(candles.size() - 1);
        }

        if (candles.size() == 0) {
            log.error("There is something wrong with candles");
        }

        log.debug("before inserting");
        candleRepository.saveAll(candles);
        log.debug("after inserting");
    }
}
