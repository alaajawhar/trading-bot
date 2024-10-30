package com.amdose.pattern.detection.detectors;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.utils.CandleUtils;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public class HighDetector {

    public static boolean isHighAt(List<CandleItemDTO> candles, int index) {
        return CandleUtils.isGreenCandle(candles, index - 1)
                && CandleUtils.isRedCandle(candles, index + 1)
                && candles.get(index).getHigh() > candles.get(index - 1).getHigh()
                && candles.get(index).getHigh() > candles.get(index + 1).getHigh();
    }
}
