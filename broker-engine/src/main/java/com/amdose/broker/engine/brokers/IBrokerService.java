package com.amdose.broker.engine.brokers;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IBrokerService {

    List<CandleEntity> getCandles(SymbolEntity symbol, TimeFrameEnum interval, Date startDate);

    void buy(SymbolEntity symbol, BigDecimal amount);

    void sell(SymbolEntity symbol, BigDecimal amount);

}
