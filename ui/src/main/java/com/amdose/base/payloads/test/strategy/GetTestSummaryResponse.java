package com.amdose.base.payloads.test.strategy;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetTestSummaryResponse {
    private Long totalWins;
    private Long totalProfit;
    private Long totalLose;
}
