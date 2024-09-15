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
import org.apache.commons.collections4.CollectionUtils;
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
    public void updateCandles(SymbolEntity symbol, TimeFrameEnum timeFrame) {
        Optional<CandleEntity> lastCandle = candleRepository.findTopBySymbolAndTimeFrameOrderByDateDesc(symbol, timeFrame);

        Date startDate = null;

        if (lastCandle.isPresent()) {
            startDate = lastCandle.get().getDate();
            startDate = timeFrame.addTime(startDate); // to only retrieve 1 record instead of 2 which was already added
        }

        List<CandleEntity> binanceCandles = this.getCandlesFromBinance(symbol, timeFrame, startDate);

        if (CollectionUtils.isEmpty(binanceCandles) == Boolean.TRUE) {
            log.error("There is something wrong with binanceCandles");
            return;
        }

        this.removeLastInvalidCandle(binanceCandles, timeFrame);

        log.debug("before inserting");
        candleRepository.saveAll(binanceCandles);
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
        parameters.put("timeInForce", "GTC");
        parameters.put("price", this.getCurrentOpenPrice(signal.getBot().getSymbol().getName()));
        parameters.put("quantity", "0.0001");
        this.sendRequest(parameters, signal);
    }

    @Override
    public void sellLimit(SignalEntity signal) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", signal.getBot().getSymbol().getName());
        parameters.put("side", "SELL");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "GTC");
        parameters.put("price", this.getCurrentOpenPrice(signal.getBot().getSymbol().getName()));
        parameters.put("quantity", "0.0001");
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

        // TODO: fix the last candle issue
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

    private List<CandleEntity> getCandlesFromBinance(SymbolEntity symbol, TimeFrameEnum interval, Date startDate) {
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


    public void removeLastInvalidCandle(List<CandleEntity> candleEntityList, TimeFrameEnum timeFrame) {

        if (CollectionUtils.isEmpty(candleEntityList) == Boolean.TRUE) {
            return;
        }

        Optional<CandleEntity> notMatchedCandle = candleEntityList.stream()
                .filter(candleEntity -> candleEntity.getTimeFrame() != timeFrame)
                .findAny();

        if (notMatchedCandle.isPresent()) {
            throw new RuntimeException("Not all candles follow the same timeFrame: [" + timeFrame + "]");
        }

        if (DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow())
                .equals(candleEntityList.get(candleEntityList.size() - 1).getDate())) {
            candleEntityList.remove(candleEntityList.size() - 1);
        }
    }


}
