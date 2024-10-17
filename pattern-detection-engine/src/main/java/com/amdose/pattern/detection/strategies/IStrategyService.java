package com.amdose.pattern.detection.strategies;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.dtos.SignalItemDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IStrategyService {

    String getName();

    List<SignalItemDTO> logic(List<CandleItemDTO> candles);

    default List<SignalItemDTO> executeStrategy(List<CandleItemDTO> candles) {
        if (CollectionUtils.isEmpty(candles)) {
            return new ArrayList<>();
        }
        return this.logic(candles);
    }
}
