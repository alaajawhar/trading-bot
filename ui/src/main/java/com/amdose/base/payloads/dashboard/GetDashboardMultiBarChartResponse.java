package com.amdose.base.payloads.dashboard;

import com.amdose.base.payloads.charts.GetMultiBarChartResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardMultiBarChartResponse {
    private GetMultiBarChartResponse todayMultiBarChart;
    private GetMultiBarChartResponse thisWeekMultiBarChart;
    private GetMultiBarChartResponse thisMonthMultiBarChart;
}
