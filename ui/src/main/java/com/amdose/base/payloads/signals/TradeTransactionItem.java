package com.amdose.base.payloads.signals;

import com.amdose.database.enums.ActionStatusEnum;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class TradeTransactionItem {
    private Long tradeId;
    private ActionStatusEnum status;
    private String brokerRequest;
    private String brokerResponse;
    private String brokerError;
    private String date;
}
