package com.amdose.pattern.detection.strategies;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IStrategyService {

    String getName();

    List<SignalEntity> logic(List<CandleEntity> candleEntities);

    default List<SignalEntity> executeStrategy(List<CandleEntity> candleEntities) {
        if (CollectionUtils.isEmpty(candleEntities)) {
            return new ArrayList<>();
        }
        return this.logic(candleEntities);
    }
}
