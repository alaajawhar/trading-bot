package com.amdose.base.payloads.dashboard;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardOverviewResponse {
    private GetDashboardSummaryResponse dashboardSummary;
    private GetDashboardLineChartResponse lineChartResponse;
    private GetDashboardRadarChartResponse radarChartResponse;
    private GetDashboardPieChartResponse pieChartResponse;
    private GetDashboardMultiBarChartResponse multiBarChartResponse;
}
