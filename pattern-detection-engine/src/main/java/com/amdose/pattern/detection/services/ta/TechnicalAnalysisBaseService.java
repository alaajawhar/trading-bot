package com.amdose.pattern.detection.services.ta;

import com.amdose.pattern.detection.helpers.HighDetectionHelper;
import com.amdose.pattern.detection.helpers.LowDetectionHelper;
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

    abstract List<Boolean> apply3BlackCrow();

    abstract List<Boolean> apply3WhiteSolider();

    public abstract List<Double> applyRsi(int timeFrame);

    public List<CandleItemDTO> applyAll() {
        this.apply3BlackCrow();
        this.apply3WhiteSolider();

        List<Boolean> threeBlackCrowsValues = this.apply3BlackCrow();
        for (int i = 0; i < candles.size(); i++) {
            this.candles.get(i).setThreeBlackCrows(threeBlackCrowsValues.get(i));
        }

        List<Boolean> threeWhiteSoliderValues = this.apply3WhiteSolider();
        for (int i = 0; i < candles.size(); i++) {
            this.candles.get(i).setThreeWhiteSoldiers(threeWhiteSoliderValues.get(i));
        }

        List<Double> rsiValues = this.applyRsi(14);
        for (int i = 0; i < candles.size(); i++) {
            this.candles.get(i).setRsiValue(rsiValues.get(i));
        }

        List<Double> closeValues = candles.stream().map(CandleItemDTO::getClose).toList();
        HighDetectionHelper highDetectionHelper = new HighDetectionHelper(closeValues, 7);
        LowDetectionHelper lowDetectionHelper = new LowDetectionHelper(closeValues, 7);

        for (int i = 0; i < candles.size(); i++) {
            this.candles.get(i).setHigh(highDetectionHelper.isHighAt(i));
            this.candles.get(i).setLow(lowDetectionHelper.isLowAt(i));
        }

        return this.candles;
    }
}
