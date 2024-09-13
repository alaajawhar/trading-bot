package com.amdose.base.controllers;

import com.amdose.base.payloads.TestRequest;
import com.amdose.base.payloads.TestResponse;
import com.amdose.base.services.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alaa Jawhar
 */
@RestController
public class TestController implements ITestController {

    @Autowired
    private TestService testService;

    public TestResponse successTest(TestRequest testRequest) {
        return testService.serve(testRequest);
    }

    public TestResponse failureTest(TestRequest testRequest) {
        throw new RuntimeException("Just for testing");
    }
}
