package com.amdose.pattern.detection.strategies.fibonacci.retracement;

import com.amdose.database.enums.SignalActionEnum;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.dtos.SignalItemDTO;
import com.amdose.pattern.detection.strategies.IStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;

import java.util.*;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
public class FibonacciRetracementStrategy implements IStrategyService {
    private static int WINDOW_SIZE = 50;
    private static double THRESHOLD = 0.1;

    @Override
    public String getName() {
        return "FIBONACCI_RETRACEMENT";
    }

    @Override
    public List<SignalItemDTO> logic(List<CandleItemDTO> candles) {
        BarSeries series = CandleItemDTO.convertToBarSeries(candles);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma50 = new SMAIndicator(closePrice, 50);
        SMAIndicator sma200 = new SMAIndicator(closePrice, 200);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);
        MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator macdSignal = new EMAIndicator(macd, 9);

        List<SignalItemDTO> signals = new ArrayList<>();
        Map<String, SignalItemDTO> openTrades = new HashMap<>();

        for (int i = 0; i < series.getBarCount(); i++) {
            boolean isUptrend = sma50.getValue(i).isGreaterThan(sma200.getValue(i));
            boolean isDowntrend = sma50.getValue(i).isLessThan(sma200.getValue(i));
            boolean bullishMomentum = macd.getValue(i).isGreaterThan(macdSignal.getValue(i));
            boolean bearishMomentum = macd.getValue(i).isLessThan(macdSignal.getValue(i));
            boolean rsiNotOverbought = rsi.getValue(i).isLessThan(DecimalNum.valueOf(70));
            boolean rsiNotOversold = rsi.getValue(i).isGreaterThan(DecimalNum.valueOf(30));

            // Long entry signal
            if (isUptrend && bullishMomentum && rsiNotOverbought && openTrades.isEmpty()) {
                String detectionId = UUID.randomUUID().toString();
                SignalItemDTO buySignal = new SignalItemDTO(
                        detectionId,
                        "{}",
                        new Date(series.getBar(i).getEndTime().toInstant().toEpochMilli()),
                        SignalActionEnum.BUY,
                        0.02
                );
                openTrades.put(detectionId, buySignal);
                signals.add(buySignal);
            }
            // Long exit signal (Sell after Buy)
            else if (openTrades.values().stream().anyMatch(s -> s.getAction() == SignalActionEnum.BUY) && isDowntrend && bearishMomentum) {
                String detectionId = openTrades.keySet().iterator().next();
                SignalItemDTO sellSignal = new SignalItemDTO(
                        detectionId,
                        "{}",
                        new Date(series.getBar(i).getEndTime().toInstant().toEpochMilli()),
                        SignalActionEnum.SELL,
                        0.02
                );
                signals.add(sellSignal);
                openTrades.remove(detectionId);
            }
        }

        return signals;
    }

}
