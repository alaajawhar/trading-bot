package com.amdose.base.payloads.test.strategy;

import com.amdose.base.payloads.dashboard.GetDashboardSummaryResponse;
import com.amdose.base.payloads.dashboard.GetLineChartResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceBaseOnTimeframesResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategyTestResponse {
    private GetDashboardSummaryResponse summaryResponse;
    private GetStrategiesPerformanceBaseOnTimeframesResponse performanceBaseOnTimeframesResponse;
    private GetLineChartResponse performanceOverTimeResponse;
}
