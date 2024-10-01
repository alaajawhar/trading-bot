package com.amdose.pattern.detection.strategies;

import com.amdose.database.entities.SignalEntity;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IStrategyService {

    String getName();

    List<SignalEntity> apply(List<CandleItemDTO> candleItemDTOS);

    default List<SignalEntity> executeStrategy(List<CandleItemDTO> candleItemDTOS) {
        if (CollectionUtils.isEmpty(candleItemDTOS)) {
            return new ArrayList<>();
        }
        return this.apply(candleItemDTOS);
    }
}
