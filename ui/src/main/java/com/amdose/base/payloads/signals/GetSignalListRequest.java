package com.amdose.base.payloads.signals;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.PaginationRequest;
import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetSignalListRequest extends PaginationRequest {
    private String detectionId;
    private TimeFrameEnum timeFrame;
    private Long botId;
    private OutcomeResultEnum outcomeResult;
    private Date date;
}
