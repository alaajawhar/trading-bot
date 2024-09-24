package com.amdose.base.payloads.dashboard;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardSummaryResponse {
    private Long totalWins;
    private Long totalProfit;
    private Long totalLose;
}
