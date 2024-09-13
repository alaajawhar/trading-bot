package com.amdose.base.repository.signals;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.signals.GetSignalListRequest;
import com.amdose.base.payloads.signals.GetSignalListResponse;
import com.amdose.base.payloads.signals.SignalItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Repository
@RequiredArgsConstructor
public class DetectedSignalsRepository {
    private final JdbcTemplate jdbcTemplate;

    public GetSignalListResponse getSignalList(GetSignalListRequest filters) {
        GetSignalListResponse response = new GetSignalListResponse();


        String query = DetectedSignalsSql.query;
        Object[] params = new Object[]{};

        if (filters.getDetectionId() != null) {
            query += " and sell_ts.detection_id = ? ";
            params = ArrayUtils.add(params, filters.getDetectionId());
        }

        if (filters.getBotId() != null) {
            query += " and sell_ts.bot_id = ? ";
            params = ArrayUtils.add(params, filters.getBotId());
        }

        if (filters.getOutcomeResult() != null) {
            if (filters.getOutcomeResult() == OutcomeResultEnum.WIN) {
                query += " and sell_candle.open - buy_candle.open > 0 ";
            }
            if (filters.getOutcomeResult() == OutcomeResultEnum.LOSE) {
                query += " and sell_candle.open - buy_candle.open < 0 ";
            }
        }

        if (filters.getTimeFrame() != null) {
            query += " AND bot.time_frame = ? AND bot.time_frame = ? ";
            params = ArrayUtils.add(params, filters.getTimeFrame().name());
            params = ArrayUtils.add(params, filters.getTimeFrame().name());
        }

        if (filters.getDate() != null) {
            query += " and DATE(sell_candle.candle_date) = ? ";
            params = ArrayUtils.add(params, filters.getDate());
        }

        query += "order by sell_candle.candle_date desc";

        String countQuery = query;
        Object[] countParams = ArrayUtils.clone(params);

        if (filters.getLimit() != null) {
            query += " LIMIT ? ";
            params = ArrayUtils.add(params, filters.getLimit());
        }

        if (filters.getOffset() != null) {
            query += " OFFSET  ? ";
            params = ArrayUtils.add(params, filters.getOffset());
        }

        List<SignalItem> signalList = jdbcTemplate.query(query, params, DetectedSignalsSql.GET_SIGNAL_LIST_RESPONSE_ROW_MAPPER);

        Long totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (" + countQuery + ") AS subquery", countParams, Long.class);

        response.setList(signalList);
        response.setTotalCount(totalCount);
        response.setOffset(filters.getOffset());

        return response;
    }
}
