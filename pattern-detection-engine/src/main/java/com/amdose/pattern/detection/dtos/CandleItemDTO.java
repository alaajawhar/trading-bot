package com.amdose.pattern.detection.dtos;

import lombok.Data;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class CandleItemDTO {
    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    private Date date;

    public static BarSeries convertToBarSeries(List<CandleItemDTO> candles) {
        BarSeries series = new BaseBarSeriesBuilder().withName("CandleSeries").build();

        for (CandleItemDTO candle : candles) {
            ZonedDateTime time = ZonedDateTime.ofInstant(candle.getDate().toInstant(), ZoneId.systemDefault());
            series.addBar(time, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose());
        }
        return series;
    }
}
