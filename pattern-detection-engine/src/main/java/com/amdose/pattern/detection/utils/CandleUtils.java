package com.amdose.pattern.detection.utils;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class CandleUtils {

    public static boolean isRedCandle(List<CandleItemDTO> candles, int index) {
        return candles.get(index).getClose() - candles.get(index).getOpen() < 0;
    }

    public static boolean isGreenCandle(List<CandleItemDTO> candles, int index) {
        return candles.get(index).getClose() - candles.get(index).getOpen() > 0;
    }
}
