package com.amdose.base.payloads.test.strategy;

import com.amdose.base.payloads.charts.GetLineChartResponse;
import com.amdose.base.payloads.charts.GetMultiBarChartResponse;
import com.amdose.base.payloads.charts.GetPieChartResponse;
import com.amdose.base.payloads.charts.GetRadarChartResponse;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategyTestResponse {
    private GetTestSummaryResponse summaryResponse;
    private GetRadarChartResponse performanceBaseOnTimeframesResponse; // TODO: rename frontend and backend
    private GetLineChartResponse performanceOverTimeResponse; // TODO: rename frontend and backend
    private GetPieChartResponse pieChartResponse;
    private GetMultiBarChartResponse multiBarChartResponse;
    private GetTestSignalsResponse signals;
}
