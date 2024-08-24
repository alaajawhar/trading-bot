package com.amdose.pattern.detection.services.ta;

import com.amdose.pattern.detection.dtos.CandleItemDTO;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public abstract class TechnicalAnalysisBaseService {

    protected List<CandleItemDTO> candles;

    public TechnicalAnalysisBaseService(List<CandleItemDTO> candles) {
        this.candles = candles;
    }

    public abstract List<Double> applyRsi(int timeFrame);

    public List<CandleItemDTO> applyAll() {

        List<Double> rsiValues = this.applyRsi(7);
        for (int i = 0; i < candles.size(); i++) {
            this.candles.get(i).setRsiValue(rsiValues.get(i));
        }
        return this.candles;
    }
}
