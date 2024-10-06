package com.amdose.base.constants;

/**
 * @author Alaa Jawhar
 */
public class UriConstants {

    // TEST
    public static final String GET_DASHBOARD_SUMMARY_TEST = "/strategy/test/summary";
    public static final String GET_DASHBOARD_STRATEGY_PERFORMANCE_OVER_PERIOD_TEST = "/strategy/test/performance/period";
    public static final String GET_DASHBOARD_BOTS_PERFORMANCE_BASE_ON_TIMEFRAME_TEST = "/strategy/test/performance/timeframes";

    // SIGNALS
    public static final String GET_SIGNAL_LIST = "/signal/list";
    public static final String GET_SIGNAL_BY_ID = "/signal/{detectionId}";

    // DROPDOWNS
    public static final String GET_STRATEGIES = "/dropdown/strategies";
    public static final String GET_OUTCOME_RESULTS = "/dropdown/outcomes";
    public static final String GET_TIMEFRAMES = "/dropdown/timeframes";
    public static final String GET_SYMBOLS = "/dropdown/symbols";

    // DASHBOARD
    public static final String GET_DASHBOARD_OVERVIEW = "/dashboard/overview";
}
