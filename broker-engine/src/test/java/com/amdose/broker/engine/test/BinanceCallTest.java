package com.amdose.broker.engine.test;

import com.amdose.utils.JsonUtils;
import com.binance.connector.client.impl.SpotClientImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public class BinanceCallTest {

    private final static String API_KEY = "sscChDXaRdTD4vo81F82awXDxRCr86AzP9mbKpdYN4PY681zdhliCou6ctDBQ8Kx";
    private final static String SECRET_KEY = "UAFUb1VBUCnTabCXNNbJSYbgykFEw741rVZNjDSbpuneglFBgcnoVQulzUNKqVOP";


    public static void main(String[] args) {
        SpotClientImpl client = new SpotClientImpl(API_KEY, SECRET_KEY);

        String serverTime = client.createMarket().time();
        Map<String, Long> o = JsonUtils.convertToObject(serverTime, Map.class);
        log.debug("Binance server time: [{}]", o.get("serverTime"));
        log.error("Difference: [{}]s", testCalculateDifferenceInSeconds(o.get("serverTime"), new Date().getTime()));

    }

    public static long testCalculateDifferenceInSeconds(long startTime, long endTime) {
        long differenceInMillis = endTime - startTime;
        return differenceInMillis / 1000;
    }
}
