package com.amdose.pattern.detection.services.signals;

import com.amdose.database.entities.SignalEntity;
import com.amdose.pattern.detection.dtos.CandleItemDTO;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IIndicatorService {

    String getName();

    List<SignalEntity> apply(List<CandleItemDTO> candleItemDTOS);

}
