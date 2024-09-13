package com.amdose.base.payloads.signals;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class SignalItem {
    private String detectionId;
    private Long botId;
    private TimeFrameEnum timeFrame;
    private OutcomeResultEnum outcomeResult;
    private Double profit;
    private Date date;
}
