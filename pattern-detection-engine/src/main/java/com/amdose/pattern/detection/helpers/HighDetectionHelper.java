package com.amdose.pattern.detection.helpers;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public class HighDetectionHelper {
    private final int lookBackPeriod;
    private final List<Boolean> highList;
    private final List<Boolean> higherHighList;
    private final List<Boolean> higherLowList;

    public HighDetectionHelper(List<Double> candles, int lookBackPeriod) {
        this.lookBackPeriod = lookBackPeriod;
        this.highList = this.detectHighs(candles);
        this.higherHighList = this.detectHigherHighs(candles, lookBackPeriod);
        this.higherLowList = this.detectHigherLows(candles, lookBackPeriod);
    }

    public boolean isHighAt(Integer index) {
        return highList.get(index);
    }

    public Boolean isHigherHighAt(Integer index) {
        return higherHighList.get(index);
    }

    public Boolean isHigherLowAt(Integer index) {
        return higherLowList.get(index);
    }

    public Optional<Integer> getLastHigherHigh(Integer startIndex) {
        if (startIndex <= lookBackPeriod) {
            return Optional.empty();
        }

        for (int i = startIndex - 1; i > startIndex - lookBackPeriod; i--) {
            if (higherHighList.get(i)) {
                return Optional.of(i);
            }
        }
        log.debug("No last higher high found in: [{}]", lookBackPeriod);
        return Optional.empty();
    }

    private List<Boolean> detectHighs(List<Double> candles) {
        List<Boolean> highList = new ArrayList<>();

        for (int i = 0; i < candles.size(); i++) {
            // Assume the candle is a isHigh until checked against neighbors
            boolean isHigh = true;

            // Check against previous candle, if it exists
            if (i > 0 && candles.get(i) >= candles.get(i - 1)) {
                isHigh = false;
            }

            // Check against next candle, if it exists
            if (i < candles.size() - 1 && candles.get(i) >= candles.get(i + 1)) {
                isHigh = false;
            }

            // Add the result to the list
            highList.add(isHigh);
        }

        return highList;
    }

    private List<Boolean> detectHigherLows(List<Double> prices, int lookBackPeriod) {
        List<Boolean> higherLows = new ArrayList<>();

        // Initialize all entries as false. We'll set them to true when a higher low is found.
        for (int i = 0; i < prices.size(); i++) {
            higherLows.add(false);
        }

        // Loop through the price list starting from 'lookBackPeriod' index
        for (int i = lookBackPeriod; i < prices.size() - 1; i++) {
            double currentPrice = prices.get(i);
            double lowestPrice = prices.get(i - lookBackPeriod);

            // Find the lowest price in the lookback period
            for (int j = i - lookBackPeriod + 1; j < i; j++) {
                if (prices.get(j) < lowestPrice) {
                    lowestPrice = prices.get(j);
                }
            }

            // Check if the current price is higher than the lowest price in the lookback period
            higherLows.set(i, currentPrice > lowestPrice && currentPrice <= prices.get(i + 1));
        }

        return higherLows;
    }

    private List<Boolean> detectHigherHighs(List<Double> prices, int lookBackPeriod) {
        List<Boolean> higherHighs = new ArrayList<>();

        // Initialize all entries as false. We'll set them to true when a higher high is found.
        for (int i = 0; i < prices.size(); i++) {
            higherHighs.add(false);
        }

        // Loop through the price list starting from 'lookBackPeriod' index
        for (int i = lookBackPeriod; i < prices.size() - 1; i++) {
            double currentPrice = prices.get(i);
            boolean isHigherHigh = true;

            // Compare the current price with the prices of the last 'lookBackPeriod' candles
            for (int j = i - lookBackPeriod; j < i; j++) {
                if (currentPrice <= prices.get(j)) {
                    // Current price is not higher than one of the previous 'lookBackPeriod' candles
                    isHigherHigh = false;
                    break;
                }
            }

            higherHighs.set(i, isHigherHigh && currentPrice >= prices.get(i + 1));
        }

        return higherHighs;
    }


}
