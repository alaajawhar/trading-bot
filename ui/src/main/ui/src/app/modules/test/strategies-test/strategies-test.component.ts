import {Component, OnInit} from '@angular/core';
import {
  DashboardSummaryItem,
  DropdownItem,
  DropdownResponse,
  GetSignalTestListResponse,
  StrategyTestRequest
} from "../../../shared/services/payloads";
import {Backend} from "../../../shared/services/backend";
import {Router} from "@angular/router";
import {LineChartModel} from "../../../shared/components/charts/line-chart/models";
import {BudgetChartModel} from "../../../shared/components/charts/budget-chart/models";
import {PieChartModel} from "../../../shared/components/charts/pie-chart/pie-chart.models";
import {MultiBarModel} from "../../../shared/components/charts/multi-bar-chart/multi-bar-chart.models";
import {convertResponseToDisabledFieldsCard, getDropdownValueFromKey} from "../../../shared/utils/function.utils";
import {DisabledFieldsCard} from "../../../shared/components/disabled-field-card/models";

@Component({
  selector: 'app-strategies-test',
  templateUrl: './strategies-test.component.html',
  styleUrls: ['./strategies-test.component.css']
})
export class StrategiesTestComponent implements OnInit {
  isLoadingData: boolean = true;

  dropdownStrategies: DropdownResponse = {
    list: []
  };

  dropdownTimeframes: DropdownResponse = {
    list: []
  };

  dropdownSymbols: DropdownResponse = {
    list: []
  };

  dropdownOutcomes: DropdownResponse = {
    list: []
  };

  timeFrameFilter: string;
  testSignalsResponse: GetSignalTestListResponse;

  dashboardSummaryResponse: DashboardSummaryItem = {
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

  /*
  * Pie Chart
  */
  pieChartData: PieChartModel = {
    list: []
  };

  /*
  * Multi-bar Chart
  */
  multiBarChart: MultiBarModel = {
    labels: [],
    list: []
  };

  constructor(private backend: Backend, private router: Router) {
  }

  ngOnInit(): void {
    this.backend.getDropdownOutcomes().subscribe(resp => {
      this.dropdownOutcomes = resp;
    })

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

      // pieChart
      const pieChartData = resp.pieChartResponse.list.map(item => ({
        name: item.chartName,
        value: item.data,
        color: item.chartColor
      }));

      this.pieChartData.list = pieChartData;


      // multi-chart
      const multiChartData = resp.multiBarChartResponse.list.map(item => ({
        name: item.chartName,
        color: item.chartColor,
        data: item.data,
      }));

      this.multiBarChart.labels = resp.multiBarChartResponse.labels;
      this.multiBarChart.list = multiChartData;

      // detected signals as table


      this.testSignalsResponse = resp.signals;

      this.isLoadingData = false;
    });
  }

  onFilterReset() {
    this.strategyTestRequest = {
      strategyId: undefined!,
      symbolId: undefined!,
    };
  }

  onDetectedSignalsFilter() {
    this.timeFrameFilter = undefined!;
  }

  selectedSignalMetaData: DisabledFieldsCard = {
    list: []
  };

  onTestSignalClick(index: number) {
    this.selectedSignalMetaData = convertResponseToDisabledFieldsCard(this.testSignalsResponse.list[index].metaData)
  }

  getValueById = (dropdowns: DropdownItem[], id: string) => getDropdownValueFromKey(dropdowns, id);

}
