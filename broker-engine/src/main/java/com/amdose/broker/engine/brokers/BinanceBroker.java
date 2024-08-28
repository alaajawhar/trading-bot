package com.amdose.broker.engine.brokers;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.utils.DateUtils;
import com.amdose.utils.JsonUtils;
import com.binance.connector.client.impl.SpotClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
public class BinanceBroker implements IBrokerService {
    private final static String API_KEY = "sscChDXaRdTD4vo81F82awXDxRCr86AzP9mbKpdYN4PY681zdhliCou6ctDBQ8Kx";
    private final static String SECRET_KEY = "UAFUb1VBUCnTabCXNNbJSYbgykFEw741rVZNjDSbpuneglFBgcnoVQulzUNKqVOP";

    private SpotClientImpl client;

    public BinanceBroker(String apiKey, String secretKey) {
        // TODO: fetch users from db and better initialization
        this.client = new SpotClientImpl(API_KEY, SECRET_KEY);
    }

    @Override
    public List<CandleEntity> getCandles(SymbolEntity symbol, TimeFrameEnum interval, Date startDate) {
        // Set parameters for the request
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol.getName());
        parameters.put("interval", interval.getBinanceInterval());
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

    @Override
    public void buy(SymbolEntity symbol, BigDecimal amount) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol.getName());
        parameters.put("side", "BUY");
        parameters.put("type", "MARKET");
        parameters.put("quantity", amount); // Amount of BTC you want to buy

        log.info("Send [BUY] request with symbol: [{}], amount: [{}]", symbol.getName(), amount);
        String result = client.createTrade().newOrder(parameters);
        log.info("[BUY] request's result: [{}]", result);
    }

    @Override
    public void sell(SymbolEntity symbol, BigDecimal amount) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol.getName());
        parameters.put("side", "SELL");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "GTC"); // Good Till Cancelled (GTC)
        parameters.put("price", this.getCurrentClosePrice(symbol));     // Price at which you want to sell
        parameters.put("quantity", amount); // Amount of BTC you want to buy

        log.info("Send [SELL] request with symbol: [{}], amount: [{}]", symbol.getName(), amount);
        String result = client.createTrade().newOrder(parameters);
        log.info("[SELL] request's result: [{}]", result);
    }


    private BigDecimal getCurrentClosePrice(SymbolEntity symbol) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol.getName());
        parameters.put("interval", TimeFrameEnum.ONE_MINUTE.getBinanceInterval());
        parameters.put("limit", 1);

        // Fetch candlestick data
        String jsonStr = client.createMarket().klines(parameters);
        List<List<Object>> jsonObj = JsonUtils.convertToObject(jsonStr, List.class);
        return new BigDecimal(String.valueOf(jsonObj.get(0).get(4)));
    }


}
