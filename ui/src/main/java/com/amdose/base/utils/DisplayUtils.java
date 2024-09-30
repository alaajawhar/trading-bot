package com.amdose.base.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class DisplayUtils {

    public static final String roundAmount(Double value) {
        return roundAmount(String.valueOf(value));
    }

    public static final String roundAmount(String value) {
        BigDecimal bd = new BigDecimal(value);
        return roundAmount(bd);
    }

    public static final String roundAmount(BigDecimal value) {
        value = value.setScale(2, RoundingMode.HALF_UP);
        return String.valueOf(value.doubleValue());
    }
}
