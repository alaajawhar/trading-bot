package com.amdose.broker.engine.test;

import com.amdose.broker.engine.brokers.BinanceBroker;
import com.amdose.broker.engine.brokers.IBrokerService;

/**
 * @author Alaa Jawhar
 */
public class BinanceCallTest {

    public static void main(String[] args) {
        IBrokerService binanceBroker = new BinanceBroker();
        binanceBroker.sellLimit(null);
    }
}
