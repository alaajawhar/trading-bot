package com.amdose.base.payloads.signals;

import com.amdose.database.enums.SignalActionEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class TradeItem {
    private Long signalId;
    private BigDecimal amount;
    private SignalActionEnum tradeAction;
    private List<TradeTransactionItem> trades = new ArrayList<>();

    public void addTrade(TradeTransactionItem item) {
        this.trades.add(item);
    }
}
