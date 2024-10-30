package com.amdose.pattern.detection.detectors;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.utils.CandleUtils;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public class LowDetector {
    public static boolean isLowAt(List<CandleItemDTO> candles, int index) {
        return CandleUtils.isRedCandle(candles, index - 1)
                && CandleUtils.isGreenCandle(candles, index + 1)
                && candles.get(index).getLow() < candles.get(index - 1).getLow()
                && candles.get(index).getLow() < candles.get(index + 1).getLow();
    }
}
