import {Component, OnInit} from '@angular/core';
import {
  DashboardSummaryResponse,
  DropdownResponse,
  GetSignalListResponse,
  StrategyTestRequest
} from "../../../shared/services/payloads";
import {Backend} from "../../../shared/services/backend";
import {Router} from "@angular/router";
import {LineChartModel} from "../../../shared/components/charts/line-chart/models";
import {BudgetChartModel} from "../../../shared/components/charts/budget-chart/models";

@Component({
  selector: 'app-strategies-test',
  templateUrl: './strategies-test.component.html',
  styleUrls: ['./strategies-test.component.css']
})
export class StrategiesTestComponent implements OnInit {
  isLoadingData: boolean = true;
  selectedPageNumber: number = 0;
  itemsPerPage: number = 15;

  dropdownStrategies: DropdownResponse = {
    list: []
  };
  dropdownTimeframes: DropdownResponse = {
    list: []
  };

  dropdownSymbols: DropdownResponse = {
    list: []
  };

  signalListResponse: GetSignalListResponse = {
    list: [], offset: 0, totalCount: 0
  };

  dashboardSummaryResponse: DashboardSummaryResponse = {
    totalLose: 0, totalProfit: 0, totalWins: 0
  };

  strategyTestRequest: StrategyTestRequest = {
    strategyId: undefined!,
    symbolId: undefined!,
  };

  /*
  * Line Chart
  */
  lineChartData: LineChartModel = {
    xValues: [],
    list: []
  };

  /*
  * Budget Chart
  */
  budgetChartData: BudgetChartModel = {
    indicators: [],
    list: []
  };


  constructor(private backend: Backend, private router: Router) {
  }

  ngOnInit(): void {
    this.backend.getDropdownStrategies().subscribe(resp => {
      this.dropdownStrategies = resp;
      this.strategyTestRequest.strategyId = Number(resp.list[0].id);
    });

    this.backend.getDropdownSymbols().subscribe(resp => {
      this.dropdownSymbols = resp;
      this.strategyTestRequest.symbolId = Number(resp.list[0].id);
    });

    this.backend.getDropdownTimeframes().subscribe(resp => {
      this.dropdownTimeframes = resp;
    });
  }

  onGo() {
    this.isLoadingData = true;
    this.backend.getTestStrategy(this.strategyTestRequest).subscribe(resp => {
      this.dashboardSummaryResponse = resp.summaryResponse;

      // line chart
      const lineChartData = resp.performanceOverTimeResponse.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        yValues: item.data
      }));

      this.lineChartData.list = lineChartData;
      this.lineChartData.xValues = resp.performanceOverTimeResponse.labels;

      // budgetChart
      this.budgetChartData.indicators = resp.performanceBaseOnTimeframesResponse.labels;
      const budgetData = resp.performanceBaseOnTimeframesResponse.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        data: item.data
      }));

      this.budgetChartData.list = budgetData;
      this.isLoadingData = false;
    });
  }

  onFilterReset() {
    this.strategyTestRequest = {
      strategyId: undefined!,
      symbolId: undefined!,
    };
  }
}
