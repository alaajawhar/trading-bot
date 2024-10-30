package com.amdose.pattern.detection.test.ta4j.detectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public class HigherHighDetectorTest {

    @Test
    @DisplayName("Should only detect the higher highs")
    public void testDetectHigherHighs() {
        List<Double> prices = Arrays.asList(1.0, 1.5, 1.2, 1.8, 1.4, 2.0, 1.3, 2.5, 1.6, 2.1);
        int startIndex = 0;
        int endIndex = prices.size() - 1;
        int window = 10;

        // Expected indices of higher highs based on the specified window
        List<Integer> expectedHigherHighIndices = Arrays.asList(1, 5, 7);


    }
}
