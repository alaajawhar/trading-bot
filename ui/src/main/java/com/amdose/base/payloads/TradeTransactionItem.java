package com.amdose.base.payloads;

import com.amdose.database.enums.ActionStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class TradeTransactionItem {
    private Long tradeId;
    private ActionStatusEnum status;
    private Date date;
}
