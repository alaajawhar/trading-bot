package com.amdose.base.payloads.dashboard;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class DashboardSummaryItem {
    private Long totalWins;
    private Long totalProfit;
    private Long totalLose;

    private Long winsRatio;
    private Long profitRatio;
    private Long loseRatio;
}
