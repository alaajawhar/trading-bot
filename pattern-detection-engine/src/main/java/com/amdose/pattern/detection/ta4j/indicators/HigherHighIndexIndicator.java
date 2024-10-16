package com.amdose.pattern.detection.ta4j.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicator;
import org.ta4j.core.num.Num;

/**
 * @author Alaa Jawhar
 * Copied from Ta4j.HighestValueIndicator
 */
public class HigherHighIndexIndicator extends AbstractIndicator<Num> {
    private final Indicator<Num> indicator;
    private final int lookbackWindow;

    public HigherHighIndexIndicator(Indicator<Num> indicator, int lookbackWindow) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
        this.lookbackWindow = lookbackWindow;
    }

    public Num getValue(int index) {
        int startIndex = Math.max(0, index - lookbackWindow);
        int highestHighIndex = -1;

        // Loop through the lookback window
        for (int i = startIndex; i <= index; i++) {
            if (isHigherHigh(i)) {
                highestHighIndex = i;
            }
        }

        if (highestHighIndex == -1) {
            return getBarSeries().numOf(Double.NaN);
        }

        return getBarSeries().numOf(highestHighIndex);
    }

    private boolean isHigherHigh(int index) {
        Num currentValue = indicator.getValue(index);

        boolean hasPrevious = (index - 1) >= 0;
        boolean hasNext = (index + 1) < getBarSeries().getBarCount();

        if (!hasPrevious || !hasNext) {
            return false;
        }

        Num previousValue = indicator.getValue(index - 1);
        Num nextValue = indicator.getValue(index + 1);

        // Higher high condition: current value must be greater than both previous and next values
        return currentValue.isGreaterThan(previousValue) && currentValue.isGreaterThan(nextValue);
    }
}
