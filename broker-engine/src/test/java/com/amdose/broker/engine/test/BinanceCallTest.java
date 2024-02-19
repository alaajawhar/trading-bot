package com.amdose.broker.engine.test;

import com.binance.connector.client.impl.SpotClientImpl;

import java.util.LinkedHashMap;

/**
 * @author Alaa Jawhar
 */
public class BinanceCallTest {
    private static final String API_KEY = "sscChDXaRdTD4vo81F82awXDxRCr86AzP9mbKpdYN4PY681zdhliCou6ctDBQ8Kx";
    private static final String SECRET_KEY = "UAFUb1VBUCnTabCXNNbJSYbgykFEw741rVZNjDSbpuneglFBgcnoVQulzUNKqVOP";

    public static void main(String[] args) {
        LinkedHashMap<String, Object> options = new LinkedHashMap<>();
        SpotClientImpl client = new SpotClientImpl(API_KEY, SECRET_KEY);

        // Set parameters for the request
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", "BTCUSDT");
        parameters.put("interval", "1d"); // e.g., 1 minute (1m), 1 hour (1h), 1 day (1d)
        parameters.put("limit", 20); // Adjust the limit as per your need

        // Fetch candlestick data
        String result = client.createMarket().klines(parameters);
        System.out.println(result);
    }
}
