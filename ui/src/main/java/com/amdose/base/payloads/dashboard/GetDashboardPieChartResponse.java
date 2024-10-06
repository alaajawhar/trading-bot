package com.amdose.base.payloads.dashboard;

import com.amdose.base.payloads.charts.GetPieChartResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardPieChartResponse {
    private GetPieChartResponse todayPieChart;
    private GetPieChartResponse thisWeekPieChart;
    private GetPieChartResponse thisMonthPieChart;
}
