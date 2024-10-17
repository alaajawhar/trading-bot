package com.amdose.pattern.detection.strategies.fibonacci.retracement;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.dtos.SignalItemDTO;
import com.amdose.pattern.detection.strategies.IStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
public class FibonacciRetracementStrategy implements IStrategyService {
    private static final int LOOK_BACK_PERIOD = 20;

    @Override
    public String getName() {
        return "FIBONACCI_RETRACEMENT";
    }

    @Override
    public List<SignalItemDTO> logic(List<CandleItemDTO> candles) {
        return new ArrayList<>();
    }

}
