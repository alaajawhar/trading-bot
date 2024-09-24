package com.amdose.base.controllers;

import com.amdose.base.payloads.dashboard.DashboardRequest;
import com.amdose.base.payloads.dashboard.GetDashboardSummaryResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceBaseOnTimeframesResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceOverTimeResponse;
import com.amdose.base.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alaa Jawhar
 */
@RestController
@RequiredArgsConstructor
public class DashboardController implements IDashboardController {

    private final DashboardService dashboardService;

    @Override
    public GetDashboardSummaryResponse getDashboardSummary(DashboardRequest request) {
        return dashboardService.getDashboardSummary(request);
    }

    @Override
    public GetStrategiesPerformanceOverTimeResponse getStrategiesPerformanceOverTime(DashboardRequest request) {
        return dashboardService.getStrategiesPerformanceOverTime(request);
    }

    @Override
    public GetStrategiesPerformanceBaseOnTimeframesResponse getStrategiesPerformanceOverTimeframes(DashboardRequest request) {
        return dashboardService.getStrategiesPerformanceOverTimeframes(request);
    }
}
