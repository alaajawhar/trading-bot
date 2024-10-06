package com.amdose.base.controllers;

import com.amdose.base.payloads.dashboard.GetDashboardOverviewResponse;
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
    public GetDashboardOverviewResponse getDashboardOverview() {
        return dashboardService.getDashboardOverview();
    }
}
