package com.amdose.broker.engine.brokers;

import com.amdose.database.entities.SignalEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;

/**
 * @author Alaa Jawhar
 */
public interface IBrokerService {

    void updateCandles(SymbolEntity symbol, TimeFrameEnum interval);

    void buyMarket(SignalEntity signal);

    void sellMarket(SignalEntity signal);

    void buyLimit(SignalEntity signal);

    void sellLimit(SignalEntity signal);

}
