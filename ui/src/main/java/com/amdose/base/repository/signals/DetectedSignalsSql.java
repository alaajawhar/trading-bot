package com.amdose.base.repository.signals;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.signals.SignalItem;
import com.amdose.database.enums.TimeFrameEnum;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Alaa Jawhar
 */
public class DetectedSignalsSql {
    public static final String query = """
            SELECT distinct sell_ts.detection_id as detection_id,
                   sell_ts.bot_id as bot_id,
                   bot.time_frame as timeframe,
                   sell_candle.candle_date            AS sell_date,
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
            WHERE sell_ts.action = 'SELL'
              AND buy_ts.action = 'BUY'
            """;

    public static final RowMapper<SignalItem> GET_SIGNAL_LIST_RESPONSE_ROW_MAPPER = (rs, rowNum) -> {
        SignalItem signalItem = new SignalItem();
        signalItem.setDetectionId(rs.getString(1));
        signalItem.setBotId(rs.getLong(2));
        signalItem.setTimeFrame(TimeFrameEnum.valueOf(rs.getString(3)));
        signalItem.setDate(rs.getTimestamp(4));
        signalItem.setProfit(rs.getDouble(5));
        signalItem.setOutcomeResult(signalItem.getProfit() > 0 ? OutcomeResultEnum.WIN : OutcomeResultEnum.LOSE);
        return signalItem;
    };
}
