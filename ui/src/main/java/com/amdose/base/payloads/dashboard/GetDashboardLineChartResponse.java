package com.amdose.base.payloads.dashboard;

import com.amdose.base.payloads.charts.GetLineChartResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetDashboardLineChartResponse {
    private GetLineChartResponse todayLineChart;
    private GetLineChartResponse thisWeekLineChart;
    private GetLineChartResponse thisMonthLineChart;
}
