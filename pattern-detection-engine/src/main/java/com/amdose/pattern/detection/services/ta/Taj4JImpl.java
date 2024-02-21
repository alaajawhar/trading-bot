package com.amdose.pattern.detection.services.ta;

import com.amdose.database.entities.CandleEntity;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public class Taj4JImpl extends TechnicalAnalysisBaseService {
    protected final BarSeries series;

    public Taj4JImpl(List<CandleItemDTO> candles) {
        super(candles);
        this.series = new BaseBarSeries();
        for (CandleEntity candle : candles) {
            ZonedDateTime time = ZonedDateTime.ofInstant(candle.getDate().toInstant(), ZoneId.systemDefault());
            this.series.addBar(time, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose());
        }
    }

    @Override
    public List<Double> applyRsi(int timeFrame) {
        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(series), timeFrame);

        List<Double> rsiValues = new ArrayList<>();
        for (int i = 0; i < series.getBarCount(); i++) {
            double rsiValue = rsi.getValue(i).doubleValue();
            rsiValues.add(rsiValue);
        }

        return rsiValues;
    }
}
