package com.amdose.pattern.detection.utils;

import com.amdose.database.entities.CandleEntity;
import lombok.experimental.UtilityClass;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class Ta4jUtils {

    public static BarSeries convertToBarSeries(List<CandleEntity> candles) {
        BarSeries series = new BaseBarSeriesBuilder().withName("CandleSeries").build();

        for (CandleEntity candle : candles) {
            ZonedDateTime time = ZonedDateTime.ofInstant(candle.getDate().toInstant(), ZoneId.systemDefault());
            series.addBar(time, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose());
        }

        return series;
    }
}
