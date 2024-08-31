package com.amdose.broker.engine.brokers;

import com.amdose.database.entities.ActionEntity;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.ActionStatusEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IActionRepository;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.utils.DateUtils;
import com.amdose.utils.ExceptionUtils;
import com.amdose.utils.JsonUtils;
import com.binance.connector.client.impl.SpotClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
public class BinanceBroker implements IBrokerService {
    private final static String API_KEY = "sscChDXaRdTD4vo81F82awXDxRCr86AzP9mbKpdYN4PY681zdhliCou6ctDBQ8Kx";
    private final static String SECRET_KEY = "UAFUb1VBUCnTabCXNNbJSYbgykFEw741rVZNjDSbpuneglFBgcnoVQulzUNKqVOP";

    private SpotClientImpl client;

    @Autowired
    private IActionRepository actionRepository;
    @Autowired
    private ICandleRepository candleRepository;

    public BinanceBroker() {
        // TODO: fetch users from db and better initialization
        this.client = new SpotClientImpl(API_KEY, SECRET_KEY);
    }

    @Override
    public void updateCandles(SymbolEntity symbol, TimeFrameEnum interval) {
        Optional<CandleEntity> lastCandle = candleRepository.findTopBySymbolAndTimeFrameOrderByDateDesc(symbol, interval);

        Date startDate = null;

        if (lastCandle.isPresent()) {
            startDate = lastCandle.get().getDate();
            startDate = interval.addTime(startDate); // to only retrieve 1 record instead of 2 which was already added
        }

        List<CandleEntity> candles = this.getCandles(symbol, interval, startDate);

        // TODO: handle first initialization when no candle exists in db. it is not stepping into the below IF case.
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

    @Override
    public void buyMarket(SignalEntity signal) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", signal.getBot().getSymbol().getName());
        parameters.put("side", "BUY");
        parameters.put("type", "MARKET");
        parameters.put("quantity", "0.0001"); // Amount of BTC you want to buy

        this.sendRequest(parameters, signal);
    }

    @Override
    public void sellMarket(SignalEntity signal) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", signal.getBot().getSymbol().getName());
        parameters.put("side", "SELL");
        parameters.put("type", "MARKET");
        parameters.put("quantity", "0.0001"); // Amount of BTC you want to buy

        this.sendRequest(parameters, signal);
    }

    @Override
    public void buyLimit(SignalEntity signal) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", signal.getBot().getSymbol().getName());
        parameters.put("side", "BUY");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "GTC"); // Good Till Cancelled (GTC)
        parameters.put("price", this.getCurrentOpenPrice(signal.getBot().getSymbol().getName()));     // Price at which you want to sell
        parameters.put("quantity", "0.0001"); // Amount of BTC you want to buy
        this.sendRequest(parameters, signal);
    }

    @Override
    public void sellLimit(SignalEntity signal) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", signal.getBot().getSymbol().getName());
        parameters.put("side", "SELL");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "GTC"); // Good Till Cancelled (GTC)
        parameters.put("price", this.getCurrentOpenPrice(signal.getBot().getSymbol().getName()));     // Price at which you want to sell
        parameters.put("quantity", "0.0001"); // Amount of BTC you want to buy
        this.sendRequest(parameters, signal);

    }

    private BigDecimal getCurrentOpenPrice(String symbol) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("interval", TimeFrameEnum.ONE_MINUTE.getBinanceInterval());
        parameters.put("limit", 1);

        // Fetch candlestick data
        String jsonStr = client.createMarket().klines(parameters);
        List<List<Object>> jsonObj = JsonUtils.convertToObject(jsonStr, List.class);
        return new BigDecimal(String.valueOf(jsonObj.get(jsonObj.size() - 1).get(1))); // return current open price
    }

    // Should not be used!
    private BigDecimal getCurrentClosePrice(String symbol) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("interval", TimeFrameEnum.ONE_MINUTE.getBinanceInterval());
        parameters.put("limit", 1);

        // Fetch candlestick data
        String jsonStr = client.createMarket().klines(parameters);
        List<List<Object>> jsonObj = JsonUtils.convertToObject(jsonStr, List.class);
        return new BigDecimal(String.valueOf(jsonObj.get(jsonObj.size() - 1).get(4))); // return current close price
    }

    private void sendRequest(LinkedHashMap<String, Object> parameters, SignalEntity signal) {

        ActionEntity actionEntity = new ActionEntity();
        actionEntity.setSignal(signal);
        actionEntity.setAmount(new BigDecimal(String.valueOf(parameters.get("quantity"))));
        actionEntity.setAddedDate(new Date());

        try {

            String requestAsJson = JsonUtils.convertToString(parameters);
            actionEntity.setBrokerRequest(requestAsJson);
            log.info("Request: [{}]", requestAsJson);
            String responseAsJson = client.createTrade().newOrder(parameters);
            log.info("Response: [{}]", responseAsJson);
            actionEntity.setBrokerResponse(responseAsJson);
            actionEntity.setStatus(ActionStatusEnum.SUCCESS);


        } catch (Exception e) {
            actionEntity.setStatus(ActionStatusEnum.ERROR);
            actionEntity.setError(ExceptionUtils.getStackTraceAsString(e));
        }

        actionRepository.save(actionEntity);
    }

    private List<CandleEntity> getCandles(SymbolEntity symbol, TimeFrameEnum interval, Date startDate) {
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


}
