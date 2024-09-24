package com.amdose.base.repository.signals;

import com.amdose.base.repository.dao.StrategyPerformanceItemDao;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Alaa Jawhar
 */
public class DashboardSql {
    public static final String GET_STRATEGY_PERFORMANCE_OVER_TIMEFRAME_BY_STRATEGY_ID_QUERY = """
            SELECT
                timeframe,
                SUM(result_difference)
            from (SELECT DISTINCT sell_ts.detection_id                    AS detection_id,
                                  bot.time_frame                          as timeframe,
                                  sell_candle.open - buy_candle.open AS result_difference
                  FROM trading_signal sell_ts
                           JOIN
                       trading_signal buy_ts
                       ON sell_ts.detection_id = buy_ts.detection_id
                           JOIN
                       bot sell_b ON sell_b.id = sell_ts.bot_id
                           JOIN
                       strategy sell_s ON sell_s.id = sell_b.strategy_id
                           JOIN
                       symbol sell_s2 ON sell_s2.id = sell_b.symbol_id
                           JOIN
                       candle sell_candle ON sell_ts.scheduled_at = sell_candle.candle_date
                           JOIN
                       bot buy_b ON buy_b.id = buy_ts.bot_id
                           JOIN
                       strategy buy_s ON buy_s.id = buy_b.strategy_id
                           JOIN
                       symbol buy_s2 ON buy_s2.id = buy_b.symbol_id
                           JOIN
                       candle buy_candle ON buy_ts.scheduled_at = buy_candle.candle_date
                           JOIN bot bot on bot.id = sell_ts.bot_id
                           JOIN strategy strategy on strategy.id = bot.strategy_id
                  WHERE sell_ts.action = 'SELL'
                    AND buy_ts.action = 'BUY'
                    and bot.strategy_id = ?
                    and sell_candle.candle_date >= CURDATE()
                  ) as subquery group by timeframe
            """;


    public static final String GET_STRATEGY_PERFORMANCE_BY_STRATEGY_ID_QUERY = """
            SELECT HOUR(sell_date) AS hour_of_day,
                   SUM(result_difference) AS total_result_difference
            FROM (
                SELECT DISTINCT sell_ts.detection_id AS detection_id,
                                sell_ts.bot_id AS bot_id,
                                bot.time_frame AS timeframe,
                                sell_candle.candle_date AS sell_date,
                                sell_candle.open - buy_candle.open AS result_difference
                FROM trading_signal sell_ts
                     JOIN trading_signal buy_ts ON sell_ts.detection_id = buy_ts.detection_id
                     JOIN bot sell_b ON sell_b.id = sell_ts.bot_id
                     JOIN strategy sell_s ON sell_s.id = sell_b.strategy_id
                     JOIN symbol sell_s2 ON sell_s2.id = sell_b.symbol_id
                     JOIN candle sell_candle ON sell_ts.scheduled_at = sell_candle.candle_date
                     JOIN bot buy_b ON buy_b.id = buy_ts.bot_id
                     JOIN strategy buy_s ON buy_s.id = buy_b.strategy_id
                     JOIN symbol buy_s2 ON buy_s2.id = buy_b.symbol_id
                     JOIN candle buy_candle ON buy_ts.scheduled_at = buy_candle.candle_date
                     JOIN bot bot ON bot.id = sell_ts.bot_id
                     JOIN strategy strategy ON strategy.id = bot.strategy_id
                WHERE sell_ts.action = 'SELL'
                  AND buy_ts.action = 'BUY'
                  AND DATE(sell_candle.candle_date) = CURDATE()
                  AND strategy.id = ?
            ) AS subquery
            GROUP BY HOUR(sell_date)
            ORDER BY hour_of_day
            """;

    public static final RowMapper<StrategyPerformanceItemDao> GET_BOT_PERFORMANCE_BY_BOT_ID_ROW_MAPPER = (rs, rowNum) -> {
        StrategyPerformanceItemDao botPerformanceDao = new StrategyPerformanceItemDao();
        botPerformanceDao.setPerformanceLabel(rs.getString(1));
        botPerformanceDao.setPerformanceValue(rs.getBigDecimal(2));
        return botPerformanceDao;
    };
}
