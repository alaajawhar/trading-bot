package com.amdose.pattern.detection.ta4j.indicators;

import org.ta4j.core.indicators.AbstractIndicator;
import org.ta4j.core.num.Num;

/**
 * @author Alaa Jawhar
 */
public class TwoPointsTrendLineIndicator extends AbstractIndicator<Num> {

    private final AbstractIndicator<Num> indicator;
    private final Num slope; // Slope of the trend line
    private final Num intercept; // Y-intercept of the trend line

    public TwoPointsTrendLineIndicator(AbstractIndicator<Num> indicator, int point1Index, int point2Index) {
        super(indicator.getBarSeries());
        this.indicator = indicator;

        // Get the values from the indicator for the two points
        Num point1Value = indicator.getValue(point1Index);
        Num point2Value = indicator.getValue(point2Index);

        // Calculate the slope of the trend line: (y2 - y1) / (x2 - x1)
        this.slope = point2Value.minus(point1Value).dividedBy(getBarSeries().numOf(point2Index - point1Index));

        // Calculate the y-intercept: y = mx + b => b = y1 - m * x1
        this.intercept = point1Value.minus(slope.multipliedBy(getBarSeries().numOf(point1Index)));
    }

    @Override
    public Num getValue(int index) {
        // Calculate the trend line value at a given index using the equation y = mx + b
        return slope.multipliedBy(getBarSeries().numOf(index)).plus(intercept);
    }
}