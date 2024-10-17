package com.amdose.pattern.detection.utils;

import com.amdose.database.entities.CandleEntity;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import lombok.experimental.UtilityClass;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class CandlesUtils {

    public static List<CandleItemDTO> convertToCandlesDto(List<CandleEntity> candles) {
        List<CandleItemDTO> response = new ArrayList<>();
        for (CandleEntity candleEntity : candles) {
            CandleItemDTO candleItemDTO = new CandleItemDTO();
            candleItemDTO.setOpen(candleEntity.getOpen());
            candleItemDTO.setHigh(candleEntity.getHigh());
            candleItemDTO.setLow(candleEntity.getLow());
            candleItemDTO.setClose(candleEntity.getClose());
            candleItemDTO.setVolume(candleEntity.getVolume());
            candleItemDTO.setDate(candleEntity.getDate());
            response.add(candleItemDTO);
        }
        return response;
    }

    public static BarSeries convertToBarSeries(List<CandleItemDTO> candles) {
        BarSeries series = new BaseBarSeriesBuilder().withName("CandleSeries").build();

        for (CandleItemDTO candle : candles) {
            ZonedDateTime time = ZonedDateTime.ofInstant(candle.getDate().toInstant(), ZoneId.systemDefault());
            series.addBar(time, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose());
        }
        return series;
    }
}
