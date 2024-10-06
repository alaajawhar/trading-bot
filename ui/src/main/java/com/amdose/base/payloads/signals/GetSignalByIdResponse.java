package com.amdose.base.payloads.signals;

import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetSignalByIdResponse {
    private String detectionId;
    private Long botId;
    private TimeFrameEnum timeframe;
    private Map<String, Object> metaData;
    private List<TradeItem> tradeList = new ArrayList<>();

    public void addTrade(TradeItem item) {
        this.tradeList.add(item);
    }
}
