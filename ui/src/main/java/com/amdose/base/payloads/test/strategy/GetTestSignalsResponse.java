package com.amdose.base.payloads.test.strategy;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetTestSignalsResponse {
    private List<TestSignalItem> list = new ArrayList<>();
}
