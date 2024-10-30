package com.amdose.base.payloads.test.strategy;

import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategyTestRequest {
    private Long strategyId;
    private Long symbolId;
    private List<TimeFrameEnum> timeframes;
    private Date fromDate;
    private Date toDate;
}
