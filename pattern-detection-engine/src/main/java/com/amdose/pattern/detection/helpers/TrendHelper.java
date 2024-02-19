package com.amdose.pattern.detection.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public class TrendHelper {

    private Function<Integer, Double> trendEquation;

    public TrendHelper(List<Double> values, Integer index1, Integer index2) {
        this.trendEquation = this.calculateTrendEquations(values, index1, index2);
    }

    public double getValueAt(Integer atIndex) {
        return trendEquation.apply(atIndex);
    }

    private Function<Integer, Double> calculateTrendEquations(List<Double> values, Integer index1, Integer index2) {
        if (index1.equals(index2)) {
            throw new RuntimeException("Indices must be distinct");
        }

        if (values.get(index1).equals(values.get(index2))) {
            log.error("y-values at those indices must be different.");
        }

        double m = (values.get(index2) - values.get(index1)) / (index2 - index1);
        double b = values.get(index1) - m * index1;

        return x -> m * x + b;
    }
}
