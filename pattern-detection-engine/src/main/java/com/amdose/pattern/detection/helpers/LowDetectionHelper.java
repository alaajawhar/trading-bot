package com.amdose.pattern.detection.helpers;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public class LowDetectionHelper {

    private final int lookBackPeriod;
    private final List<Boolean> lowList;
    private final List<Boolean> lowerHighList;
    private final List<Boolean> lowerLowList;

    public LowDetectionHelper(List<Double> candles, int lookBackPeriod) {
        this.lookBackPeriod = lookBackPeriod;
        this.lowList = this.detectLows(candles);
        this.lowerHighList = this.detectLowerHighs(candles, lookBackPeriod);
        this.lowerLowList = this.detectLowerLows(candles, lookBackPeriod);
    }

    public boolean isLowAt(Integer index) {
        return lowList.get(index);
    }

    public Boolean isLowerLowAt(Integer index) {
        return lowerLowList.get(index);
    }

    public Boolean isLowerHighAt(Integer index) {
        return lowerHighList.get(index);
    }

    public Optional<Integer> getLastLowerLow(int startIndex) {
        if (startIndex <= lookBackPeriod) {
            return Optional.empty();
        }

        for (int i = startIndex - 1; i > startIndex - lookBackPeriod; i--) {
            if (lowerLowList.get(i)) {
                return Optional.of(i);
            }
        }
        log.debug("No last lower low found in: [{}]", lookBackPeriod);
        return Optional.empty();
    }

    private List<Boolean> detectLows(List<Double> candles) {
        List<Boolean> lowList = new ArrayList<>();

        for (int i = 0; i < candles.size(); i++) {
            // Assume the candle is a isLow until checked against neighbors
            boolean isLow = true;

            // Check against previous candle, if it exists
            if (i > 0 && candles.get(i) <= candles.get(i - 1)) {
                isLow = false;
            }

            // Check against next candle, if it exists
            if (i < candles.size() - 1 && candles.get(i) <= candles.get(i + 1)) {
                isLow = false;
            }

            // Add the result to the list
            lowList.add(isLow);
        }

        return lowList;
    }

    private List<Boolean> detectLowerHighs(List<Double> prices, int lookBackPeriod) {
        List<Boolean> lowerHighs = new ArrayList<>();

        // Initialize all entries as false. We'll set them to true when a lower high is found.
        for (int i = 0; i < prices.size(); i++) {
            lowerHighs.add(false);
        }

        // Loop through the price list starting from 'lookBackPeriod' index
        for (int i = lookBackPeriod; i < prices.size() - 1; i++) {
            double currentPrice = prices.get(i);
            double highestPrice = prices.get(i - lookBackPeriod);

            // Find the highest price in the lookback period
            for (int j = i - lookBackPeriod + 1; j < i; j++) {
                if (prices.get(j) > highestPrice) {
                    highestPrice = prices.get(j);
                }
            }

            // Check if the current price is lower than the highest price in the lookBack period
            lowerHighs.set(i, currentPrice < highestPrice && currentPrice >= prices.get(i + 1));
        }

        return lowerHighs;
    }

    private List<Boolean> detectLowerLows(List<Double> prices, int lookBackPeriod) {
        List<Boolean> lowerLows = new ArrayList<>();

        // Initialize all entries as false. We'll set them to true when a lower low is found.
        for (int i = 0; i < prices.size(); i++) {
            lowerLows.add(false);
        }

        // Loop through the price list starting from 'lookBackPeriod' index
        for (int i = lookBackPeriod; i < prices.size() - 1; i++) {
            double currentPrice = prices.get(i);
            boolean isLowerLow = true;

            // Compare the current price with the prices of the last 'lookBackPeriod' candles
            for (int j = i - lookBackPeriod; j < i; j++) {
                if (currentPrice >= prices.get(j)) {
                    // Current price is not lower than one of the previous 'lookBackPeriod' candles
                    isLowerLow = false;
                    break;
                }
            }

            lowerLows.set(i, isLowerLow && currentPrice < prices.get(i + 1));
        }

        return lowerLows;
    }


}
