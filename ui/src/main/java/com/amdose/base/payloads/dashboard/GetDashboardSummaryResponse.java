package com.amdose.base.payloads.dashboard;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardSummaryResponse {
    private DashboardSummaryItem todaySummary;
    private DashboardSummaryItem thisWeekSummary;
    private DashboardSummaryItem thisMonthSummary;
}
