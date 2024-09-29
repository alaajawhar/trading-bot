package com.amdose.base.controllers;

import com.amdose.base.payloads.test.strategy.GetStrategyTestRequest;
import com.amdose.base.payloads.test.strategy.GetStrategyTestResponse;
import com.amdose.base.services.test.StrategyTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alaa Jawhar
 */
@RestController
public class TestController implements ITestController {

    @Autowired
    private StrategyTestService strategyTestService;

    @Override
    public GetStrategyTestResponse getStrategyTest(GetStrategyTestRequest request) {
        return strategyTestService.getStrategyTest(request);
    }
}
