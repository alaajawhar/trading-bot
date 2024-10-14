package com.amdose.pattern.detection.strategies.fibonacci.retracement;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.SignalActionEnum;
import com.amdose.pattern.detection.strategies.IStrategyService;
import com.amdose.pattern.detection.utils.Ta4jUtils;
import com.amdose.utils.DateUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.indicators.helpers.*;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
public class FibonacciRetracementStrategy implements IStrategyService {
    private static final int LOOK_BACK_PERIOD = 20;

    @Override
    public String getName() {
        return "FIBONACCI_RETRACEMENT";
    }

    @Override
    public List<SignalEntity> logic(List<CandleEntity> candleEntities) {
        BarSeries series = Ta4jUtils.convertToBarSeries(candleEntities);

        HighPriceIndicator highPrice = new HighPriceIndicator(series);
        LowPriceIndicator lowPrice = new LowPriceIndicator(series);

        HighestValueIndicator higherHighIndicator = new HighestValueIndicator(highPrice, LOOK_BACK_PERIOD);
        LowestValueIndicator lowerLowIndicator = new LowestValueIndicator(lowPrice, LOOK_BACK_PERIOD);

        // returns the highest high price from last bar index
        Num highestHigh = higherHighIndicator.getValue(series.getEndIndex());
        Num lowestLow = lowerLowIndicator.getValue(series.getEndIndex());
        // Calculate Fibonacci retracement levels
        FibonacciRetracement fibRetracement = new FibonacciRetracement(highestHigh.doubleValue(), lowestLow.doubleValue());

        // Build the Fibonacci strategy
        Strategy strategy = this.buildFibonacciStrategy(series, fibRetracement);

        BaseTradingRecord tradingRecord = new BaseTradingRecord();
        for (int i = 0; i < series.getBarCount(); i++) {
            if (strategy.shouldEnter(i)) {
                tradingRecord.enter(i, series.getBar(i).getClosePrice(), series.getBar(i).getVolume());
            } else if (strategy.shouldExit(i)) {
                tradingRecord.exit(i, series.getBar(i).getClosePrice(), series.getBar(i).getVolume());
            }
        }

        List<SignalEntity> signalEntities = new ArrayList<>();
        for (int i = 0; i < series.getBarCount(); i++) {
            if (strategy.shouldEnter(i)) {
                SignalEntity signal = createSignalEntity(series, i, SignalActionEnum.BUY);
                signalEntities.add(signal);
            } else if (strategy.shouldExit(i)) {
                SignalEntity signal = createSignalEntity(series, i, SignalActionEnum.SELL);
                signalEntities.add(signal);
            }
        }

        return signalEntities;
    }

    private Strategy buildFibonacciStrategy(BarSeries series, FibonacciRetracement fibLevels) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // Entry rule: Price crosses below 38.2% Fibonacci level (buy)
        Rule entryRule = new CrossedDownIndicatorRule(closePrice, fibLevels.getLevel38_2());

        // Exit rule: Price crosses above 61.8% Fibonacci level (sell)
        Rule exitRule = new CrossedUpIndicatorRule(closePrice, fibLevels.getLevel61_8());

        return new BaseStrategy(entryRule, exitRule);
    }

    private SignalEntity createSignalEntity(BarSeries series, int index, SignalActionEnum action) {
        SignalEntity signalEntity = new SignalEntity();
        signalEntity.setAction(action);
        signalEntity.setMetaData("Price: " + series.getBar(index).getClosePrice());  // Example metadata
        signalEntity.setScheduledAt(Date.from(series.getBar(index).getEndTime().toInstant()));  // Use bar time
        signalEntity.setRisk(0.01);  // Example risk; customize based on your logic
        signalEntity.setAddedDate(DateUtils.getNow());  // Set the current time as the added date
        return signalEntity;
    }

    @Data
    @RequiredArgsConstructor
    private class FibonacciRetracement {
        private final double high;
        private final double low;

        public double getLevel(double percentage) {
            return high - (percentage * (high - low));
        }

        public double getLevel23_6() {
            return getLevel(0.236);
        }

        public double getLevel38_2() {
            return getLevel(0.382);
        }

        public double getLevel50() {
            return getLevel(0.500);
        }

        public double getLevel61_8() {
            return getLevel(0.618);
        }
    }
}
