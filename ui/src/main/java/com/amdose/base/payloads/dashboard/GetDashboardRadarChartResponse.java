package com.amdose.base.payloads.dashboard;

import com.amdose.base.payloads.charts.GetRadarChartResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardRadarChartResponse {
    private GetRadarChartResponse todayRadarChart;
    private GetRadarChartResponse thisWeekRadarChart;
    private GetRadarChartResponse thisMonthRadarChart;
}
