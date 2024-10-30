package com.amdose.pattern.detection.detectors;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.dtos.detectors.MWResult;
import org.ta4j.core.BarSeries;

import java.util.List;
import java.util.Optional;

/**
 * @author Alaa Jawhar
 */
@Deprecated
public class MWDetector {

    public static MWResult isDoubleBottom(BarSeries series, int startIndex, int endIndex, double threshold) {
        return null;
    }

    public static Optional<MWResult> isDoubleTop(List<CandleItemDTO> candles, int startIndex, int endIndex, double threshold) {
        // Ensure indices are within the bounds of the prices list
        if (startIndex < 1 || endIndex >= candles.size() || startIndex >= endIndex) {
            return Optional.empty();
        }

        boolean foundFirstLow = false;
        boolean foundFirstHigh = false;
        boolean foundSecondLow = false;
        boolean foundSecondHigh = false;

        Integer firstLowIndex = null;
        Integer firstHighIndex = null;
        Integer secondLowIndex = null;
        Integer secondHighIndex = null;
        Integer thirdLowIndex = null;

        // Traverse through the price data
        for (int i = startIndex; i < endIndex; i++) {
            // Check for the first low
            if (!foundFirstLow && LowDetector.isLowAt(candles, i)) {
                firstLowIndex = i;
                foundFirstLow = true;
            }
            // Check for the first high after the first low
            else if (foundFirstLow && !foundFirstHigh && HighDetector.isHighAt(candles, i)) {
                firstHighIndex = i;
                foundFirstHigh = true;
            }
            // Check for the second low after the first high
            else if (foundFirstHigh && !foundSecondLow && LowDetector.isLowAt(candles, i)) {
                secondLowIndex = i;
                foundSecondLow = true;
            }
            // Check for the second high after the second low
            else if (foundSecondLow && !foundSecondHigh && HighDetector.isHighAt(candles, i)) {
                secondHighIndex = i;
                foundSecondHigh = true;
            }
            // Check for the third low after the second high
            else if (foundSecondHigh && LowDetector.isLowAt(candles, i)) {
                thirdLowIndex = i;

                // Check if the second high is sufficiently distinct from the first high
                if (Math.abs(candles.get(secondHighIndex).getHigh() - candles.get(firstHighIndex).getHigh()) > threshold) {
                    // Create and return the MWResult object
                    return Optional.of(new MWResult(
                            firstHighIndex,
                            secondHighIndex,
                            firstLowIndex,
                            secondLowIndex,
                            thirdLowIndex
                    ));
                }
            }
        }

        return Optional.empty(); // No double top pattern detected
    }

}
