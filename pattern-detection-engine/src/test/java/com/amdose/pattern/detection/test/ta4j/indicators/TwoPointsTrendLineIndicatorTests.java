package com.amdose.pattern.detection.test.ta4j.indicators;

import com.amdose.pattern.detection.ta4j.indicators.TwoPointsTrendLineIndicator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alaa Jawhar
 */
public class TwoPointsTrendLineIndicatorTests {

    private static BarSeries series;

    @BeforeAll
    public static void setUp() {
        series = new BaseBarSeriesBuilder().withName("Test Series").build();

        // Adding bars (open, high, low, close, volume)
        series.addBar(ZonedDateTime.now().plusMinutes(0), 100, 95, 98, 102, 10000);
        series.addBar(ZonedDateTime.now().plusMinutes(5), 102, 100, 115, 104, 12000);
        series.addBar(ZonedDateTime.now().plusMinutes(10), 104, 98, 110, 106, 15000);
        series.addBar(ZonedDateTime.now().plusMinutes(15), 106, 105, 95, 108, 20000);
        series.addBar(ZonedDateTime.now().plusMinutes(20), 108, 110, 106, 110, 25000);
        series.addBar(ZonedDateTime.now().plusMinutes(25), 110, 98, 103, 112, 28000);

    }

    @Test
    @DisplayName("Values at specified indexes should match exactly the values of the trend line")
    public void test1() {
        HighPriceIndicator highPriceIndicator = new HighPriceIndicator(series);
        TwoPointsTrendLineIndicator twoPointsTrendLineIndicator = new TwoPointsTrendLineIndicator(highPriceIndicator, 2, 3);

        assertEquals(twoPointsTrendLineIndicator.getValue(2), series.getBar(2).getHighPrice());
        assertEquals(twoPointsTrendLineIndicator.getValue(3), series.getBar(3).getHighPrice());
    }

    @Test
    @DisplayName("Values should match the high prices trend equation: y = 7x + 84")
    public void test2() {
        HighPriceIndicator highPriceIndicator = new HighPriceIndicator(series);
        TwoPointsTrendLineIndicator twoPointsTrendLineIndicator = new TwoPointsTrendLineIndicator(highPriceIndicator, 2, 3);

        assertEquals(twoPointsTrendLineIndicator.getValue(0), series.numOf(84));
        assertEquals(twoPointsTrendLineIndicator.getValue(4), series.numOf(112));
        assertEquals(twoPointsTrendLineIndicator.getValue(5), series.numOf(119));
    }


}
