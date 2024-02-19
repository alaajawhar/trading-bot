package com.amdose.broker.engine.services;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.enums.TimeFrameEnum;

import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IBrokerService {

    List<CandleEntity> getCandles(String symbol, TimeFrameEnum interval, Date startDate);

}
