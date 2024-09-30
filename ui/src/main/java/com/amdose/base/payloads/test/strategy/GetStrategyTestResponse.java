package com.amdose.base.payloads.test.strategy;

import com.amdose.base.payloads.dashboard.*;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategyTestResponse {
    private GetDashboardSummaryResponse summaryResponse;
    private GetStrategiesPerformanceBaseOnTimeframesResponse performanceBaseOnTimeframesResponse;
    private GetLineChartResponse performanceOverTimeResponse;
    private GetPieChartResponse pieChartResponse;
    private GetMultiBarChartResponse multiBarChartResponse;
    private GetTestSignalsResponse signals;
}
