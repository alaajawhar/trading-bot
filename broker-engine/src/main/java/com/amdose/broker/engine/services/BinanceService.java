package com.amdose.broker.engine.services;

import com.amdose.broker.engine.IBrokerService;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.utils.DateUtils;
import com.amdose.utils.JsonUtils;
import com.binance.connector.client.impl.SpotClientImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Service
public class BinanceService implements IBrokerService {
    private static final String API_KEY = "sscChDXaRdTD4vo81F82awXDxRCr86AzP9mbKpdYN4PY681zdhliCou6ctDBQ8Kx";
    private static final String SECRET_KEY = "UAFUb1VBUCnTabCXNNbJSYbgykFEw741rVZNjDSbpuneglFBgcnoVQulzUNKqVOP";

    @Override
    public List<CandleEntity> getCandles(String symbol, TimeFrameEnum interval, Date startDate) {
        SpotClientImpl client = new SpotClientImpl(API_KEY, SECRET_KEY);

        // Set parameters for the request
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("interval", this.getBinanceInterval(interval));
        parameters.put("limit", 1000);

        if (startDate != null) {
            parameters.put("startTime", startDate.getTime());
        }

        // Fetch candlestick data
        String jsonStr = client.createMarket().klines(parameters);
        List<List<Object>> jsonObj = JsonUtils.convertToObject(jsonStr, List.class);

        List<CandleEntity> response = new ArrayList<>();
        for (List<Object> doubles : jsonObj) {
            CandleEntity brokerCandle = new CandleEntity();
            brokerCandle.setDate(DateUtils.convertLongToDate((long) doubles.get(0)));
            brokerCandle.setOpen(Double.parseDouble((String) doubles.get(1)));
            brokerCandle.setHigh(Double.parseDouble((String) doubles.get(2)));
            brokerCandle.setLow(Double.parseDouble((String) doubles.get(3)));
            brokerCandle.setClose(Double.parseDouble((String) doubles.get(4)));
            brokerCandle.setVolume(Double.parseDouble((String) doubles.get(5)));
            brokerCandle.setSymbol(symbol);
            brokerCandle.setTimeFrame(interval);
            response.add(brokerCandle);
        }

        return response;
    }

    private String getBinanceInterval(TimeFrameEnum interval) {
        return switch (interval) {
            case ONE_MINUTE -> "1m";
            case THREE_MINUTES -> "3m";
            case FIFTEEN_MINUTES -> "15m";
            case ONE_HOUR -> "1h";
            case FOUR_HOURS -> "4h";
            case ONE_DAY -> "1d";
        };
    }
}
