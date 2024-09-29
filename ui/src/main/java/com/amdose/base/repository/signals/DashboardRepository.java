package com.amdose.base.repository.signals;

import com.amdose.base.models.enums.DashboardFilterEnum;
import com.amdose.base.payloads.dashboard.DashboardRequest;
import com.amdose.base.repository.dao.StrategyPerformanceItemDao;
import com.amdose.base.utils.RepositoryUtils;
import com.amdose.database.enums.TimeFrameEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author Alaa Jawhar
 */
@Repository
@RequiredArgsConstructor
public class DashboardRepository {
    private final JdbcTemplate jdbcTemplate;


    public Long getTotalLose(DashboardRequest filters) {
        String query = DetectedSignalsSql.query;
        Object[] params = new Object[]{};

        if (filters.getFilter() == DashboardFilterEnum.TODAY) {
            query += " and sell_candle.open - buy_candle.open < 0 ";
            query += " and sell_candle.candle_date >= CURDATE() ";
        }


        return jdbcTemplate.queryForObject(RepositoryUtils.getCountQuery(query), params, Long.class);
    }

    public Long getTotalWins(DashboardRequest filters) {
        String query = DetectedSignalsSql.query;
        Object[] params = new Object[]{};

        query += " and sell_candle.open - buy_candle.open > 0 ";
        if (filters.getFilter() == DashboardFilterEnum.TODAY) {
            query += " and sell_candle.candle_date >= CURDATE() ";
        }

        if (filters.getFilter() == DashboardFilterEnum.THIS_WEEK) {
            query += " AND sell_candle.candle_date >= CURDATE() - INTERVAL WEEKDAY(CURDATE()) DAY ";
        }

        return jdbcTemplate.queryForObject(RepositoryUtils.getCountQuery(query), params, Long.class);
    }

    public Double getTotalProfit(DashboardRequest filters) {
        String query = DetectedSignalsSql.query;
        Object[] params = new Object[]{};

        if (filters.getFilter() == DashboardFilterEnum.TODAY) {
            query += " and sell_candle.candle_date >= CURDATE() ";
        }


        Double totalProfit = jdbcTemplate.queryForObject(
                RepositoryUtils.getSumQuery(query, "result_difference"), params, Double.class);

        if (totalProfit == null) {
            return 0d;
        }

        return totalProfit;
    }

    public List<StrategyPerformanceItemDao> getStrategyTodaysPerformanceByStrategyId(DashboardRequest filters, Long strategyId) {
        List<StrategyPerformanceItemDao> list = new ArrayList<>();

        String query = DashboardSql.GET_STRATEGY_PERFORMANCE_BY_STRATEGY_ID_QUERY;
        Object[] params = new Object[]{strategyId};

        List<StrategyPerformanceItemDao> strategyPerformanceDaoList = jdbcTemplate.query(query
                , params
                , DashboardSql.GET_BOT_PERFORMANCE_BY_BOT_ID_ROW_MAPPER);

        Map<String, BigDecimal> strategyPerformanceDaoMap = strategyPerformanceDaoList.stream()
                .collect(toMap(StrategyPerformanceItemDao::getPerformanceLabel, StrategyPerformanceItemDao::getPerformanceValue));

        for (int i = 0; i < 24; i++) {
            StrategyPerformanceItemDao strategyPerformanceItemDao = new StrategyPerformanceItemDao();
            strategyPerformanceItemDao.setPerformanceLabel(String.format("%02d:00", i));
            strategyPerformanceItemDao.setPerformanceValue(
                    strategyPerformanceDaoMap.get(String.valueOf(i)) == null ? BigDecimal.ZERO : strategyPerformanceDaoMap.get(String.valueOf(i)));
            list.add(strategyPerformanceItemDao);
        }

        return list;
    }


    public List<StrategyPerformanceItemDao> getStrategyOverTimeframesByStrategyId(DashboardRequest filters, Long strategyId) {
        List<StrategyPerformanceItemDao> list = new ArrayList<>();

        String query = DashboardSql.GET_STRATEGY_PERFORMANCE_OVER_TIMEFRAME_BY_STRATEGY_ID_QUERY;
        Object[] params = new Object[]{strategyId};

        List<StrategyPerformanceItemDao> strategyPerformanceDaoList = jdbcTemplate.query(query
                , params
                , DashboardSql.GET_BOT_PERFORMANCE_BY_BOT_ID_ROW_MAPPER);

        Map<String, BigDecimal> strategyPerformanceDaoMap = strategyPerformanceDaoList.stream()
                .collect(toMap(StrategyPerformanceItemDao::getPerformanceLabel, StrategyPerformanceItemDao::getPerformanceValue));

        for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
            StrategyPerformanceItemDao strategyPerformanceItemDao = new StrategyPerformanceItemDao();
            strategyPerformanceItemDao.setPerformanceLabel(timeframe.getLabel());
            strategyPerformanceItemDao.setPerformanceValue(
                    strategyPerformanceDaoMap.get(timeframe.name()) == null ? BigDecimal.ZERO : strategyPerformanceDaoMap.get(timeframe.name())
            );
            list.add(strategyPerformanceItemDao);
        }

        return list;
    }

    public List<StrategyPerformanceItemDao> getStrategyPercentagesByStrategyId(DashboardRequest filters, Long strategyId) {
        List<StrategyPerformanceItemDao> list = new ArrayList<>();

        String query = DashboardSql.GET_STRATEGY_PERFORMANCE_OVER_TIMEFRAME_BY_STRATEGY_ID_QUERY;
        Object[] params = new Object[]{strategyId};

        List<StrategyPerformanceItemDao> strategyPerformanceDaoList = jdbcTemplate.query(query
                , params
                , DashboardSql.GET_BOT_PERFORMANCE_BY_BOT_ID_ROW_MAPPER);

        Map<String, BigDecimal> strategyPerformanceDaoMap = strategyPerformanceDaoList.stream()
                .collect(toMap(StrategyPerformanceItemDao::getPerformanceLabel, StrategyPerformanceItemDao::getPerformanceValue));

        for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
            StrategyPerformanceItemDao strategyPerformanceItemDao = new StrategyPerformanceItemDao();
            strategyPerformanceItemDao.setPerformanceLabel(timeframe.getLabel());
            strategyPerformanceItemDao.setPerformanceValue(
                    strategyPerformanceDaoMap.get(timeframe.name()) == null ? BigDecimal.ZERO : strategyPerformanceDaoMap.get(timeframe.name())
            );
            list.add(strategyPerformanceItemDao);
        }

        return list;
    }
}
