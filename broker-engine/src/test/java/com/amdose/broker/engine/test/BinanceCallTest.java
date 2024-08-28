package com.amdose.broker.engine.test;

import com.amdose.broker.engine.brokers.BinanceBroker;
import com.amdose.broker.engine.brokers.IBrokerService;
import com.amdose.database.entities.SymbolEntity;

import java.math.BigDecimal;

/**
 * @author Alaa Jawhar
 */
public class BinanceCallTest {

    public static void main(String[] args) {
        IBrokerService binanceBroker = new BinanceBroker(null, null);

        SymbolEntity symbol = new SymbolEntity();
        symbol.setName("BTCUSDT");
//        binanceBroker.buy(symbol, new BigDecimal("0.00016"));
        binanceBroker.sell(symbol, new BigDecimal("0.00016"));
    }
}
