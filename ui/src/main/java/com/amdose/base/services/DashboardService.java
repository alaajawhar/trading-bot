package com.amdose.base.services;

import com.amdose.base.models.enums.DashboardFilterEnum;
import com.amdose.base.payloads.dashboard.*;
import com.amdose.base.repository.dao.StrategyPerformanceItemDao;
import com.amdose.base.repository.signals.DashboardRepository;
import com.amdose.base.repository.signals.DetectedSignalsRepository;
import com.amdose.database.entities.StrategyEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IBotRepository;
import com.amdose.database.repositories.IStrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alaa Jawhar
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DetectedSignalsRepository detectedSignalsRepository;
    private final DashboardRepository dashboardRepository;
    private final IBotRepository botRepository;
    private final IStrategyRepository strategyRepository;

    public GetDashboardSummaryResponse getDashboardSummary(DashboardRequest request) {
        Long totalLose = dashboardRepository.getTotalLose(request);
        Long totalWins = dashboardRepository.getTotalWins(request);
        double totalProfit = dashboardRepository.getTotalProfit(request);

        GetDashboardSummaryResponse response = new GetDashboardSummaryResponse();
        response.setTotalWins(totalWins);
        response.setTotalProfit((long) totalProfit);
        response.setTotalLose(totalLose);
        return response;
    }

    public GetLineChartResponse getStrategiesPerformanceOverTime(DashboardRequest request) {
        GetLineChartResponse response = new GetLineChartResponse();

        switch (request.getFilter()) {
            case TODAY -> response.setLabels(List.of("00:00", "01:00", "02:00", "03:00", "04:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"));
            case THIS_WEEK -> response.setLabels(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
            case THIS_MONTH -> response.setLabels(List.of("Week 1", "Week 2", "Week 3", "Week 4"));
        }

        List<ChartItem> list = new ArrayList<>();

        for (StrategyEntity strategyItem : strategyRepository.findAll()) {
            ChartItem chartItem = new ChartItem();
            chartItem.setChartName(strategyItem.getName());
            chartItem.setChartColor("#5a0bba");

            if (request.getFilter() == DashboardFilterEnum.TODAY) {
                List<StrategyPerformanceItemDao> todaysPerformanceList = dashboardRepository.getStrategyTodaysPerformanceByStrategyId(request, strategyItem.getId());
                List<Double> collect = todaysPerformanceList.stream().map(StrategyPerformanceItemDao::getPerformanceValue).mapToDouble(BigDecimal::doubleValue).boxed().collect(Collectors.toList());
                collect.add(0, 0.0d); // just for chart beginning. TODO: find a better solution
                chartItem.setData(collect);
            }

            list.add(chartItem);
        }

        response.setList(list);
        return response;
    }

    public GetStrategiesPerformanceBaseOnTimeframesResponse getStrategiesPerformanceOverTimeframes(DashboardRequest request) {
        GetStrategiesPerformanceBaseOnTimeframesResponse response = new GetStrategiesPerformanceBaseOnTimeframesResponse();
        List<ChartItem> list = new ArrayList<>();

        response.setLabels(Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).collect(Collectors.toList()));

        for (StrategyEntity strategyItem : strategyRepository.findAll()) {
            ChartItem chartItem = new ChartItem();
            chartItem.setChartName(strategyItem.getName());
            chartItem.setChartColor("#5a0bba");

            if (request.getFilter() == DashboardFilterEnum.TODAY) {
                List<StrategyPerformanceItemDao> todaysPerformanceList = dashboardRepository.getStrategyOverTimeframesByStrategyId(request, strategyItem.getId());
                List<Double> dataList = todaysPerformanceList.stream().map(StrategyPerformanceItemDao::getPerformanceValue).mapToDouble(BigDecimal::doubleValue).boxed().collect(Collectors.toList());
                Double maxValue = dataList.stream().max(Comparator.naturalOrder()).get();
                chartItem.setData(dataList);
            }

            list.add(chartItem);
        }


        response.setList(list);
        return response;
    }
}
