package com.amdose.base.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class DisplayUtils {

    public static String roundAmountToString(Double value) {
        return roundAmountToString(String.valueOf(value));
    }

    public static String roundAmountToString(String value) {
        BigDecimal bd = new BigDecimal(value);
        return roundAmountToString(bd);
    }

    public static String roundAmountToString(BigDecimal value) {
        value = value.setScale(2, RoundingMode.HALF_UP);
        return String.valueOf(value.doubleValue());
    }


    public static Double roundAmountToDouble(Double value) {
        return roundAmountToDouble(String.valueOf(value));
    }

    public static Double roundAmountToDouble(String value) {
        BigDecimal bd = new BigDecimal(value);
        return roundAmountToDouble(bd);
    }

    public static Double roundAmountToDouble(BigDecimal value) {
        value = value.setScale(2, RoundingMode.HALF_UP);
        return value.doubleValue();
    }
}
