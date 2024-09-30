package com.amdose.base.payloads.test.strategy;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class TestSignalItem {
    private TimeFrameEnum timeframe;
    private OutcomeResultEnum outcomeResult;
    private String profit;
    private Object metaData;
    private Date date;
}
