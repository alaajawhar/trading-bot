import {Component, OnInit} from '@angular/core';
import {BudgetChartModel} from "../../shared/components/charts/budget-chart/models";
import {LineChartModel} from "../../shared/components/charts/line-chart/models";
import {Backend} from "../../shared/services/backend";
import {Router} from "@angular/router";
import {
  DashboardSummaryItem,
  GetDashboardOverviewResponse,
  GetLineChartResponse,
  GetRadarChartResponse,
  MultiBarChartResponse,
  PieChartResponse
} from "../../shared/services/payloads";
import {MultiBarModel} from "../../shared/components/charts/multi-bar-chart/multi-bar-chart.models";
import {PieChartModel} from "../../shared/components/charts/pie-chart/pie-chart.models";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  selectedFilterDisplay: string = 'Today'

  getDashboardOverviewResponse: GetDashboardOverviewResponse = {
    multiBarChartResponse: undefined!,
    dashboardSummary: undefined!,
    lineChartResponse: undefined!,
    radarChartResponse: undefined!,
    pieChartResponse: undefined!
  }

  /*
  * SUMMARY
  */
  dashboardSummaryResponse: DashboardSummaryItem = {
    totalLose: 0, totalProfit: 0, totalWins: 0
  };

  /*
  * Line Chart
  */
  lineChartModel: LineChartModel = {
    xValues: [],
    list: []
  };

  /*
  * Budget Chart
  */
  radarChartModel: BudgetChartModel = {
    indicators: [],
    list: []
  };

  /*
  * Multi-bar Chart
  */
  multiBarChartModel: MultiBarModel = {
    labels: [],
    list: []
  };


  pieChartModel: PieChartModel = {
    list: []
  };

  constructor(private backend: Backend, private router: Router) {
  }

  ngOnInit(): void {
    this.backend.getDashboardOverview().subscribe(resp => {
      this.getDashboardOverviewResponse = resp;
      // this.setFilterTo('TODAY') // TODO: make it work, instead of the below
      /*
      * SUMMARY
      */
      this.dashboardSummaryResponse = resp.dashboardSummary.todaySummary;

      /*
      * LINE CHART
      */
      this.lineChartModel.xValues = resp.lineChartResponse.todayLineChart.labels;

      const lineChartData = resp.lineChartResponse.todayLineChart.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        yValues: item.data
      }));

      this.lineChartModel.list = lineChartData;

      /*
      * RADAR CHART
      */
      this.radarChartModel.indicators = resp.radarChartResponse.todayRadarChart.labels;

      const radarChartData = resp.radarChartResponse.todayRadarChart.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        data: item.data
      }));

      this.radarChartModel.list = radarChartData;

      /*
      * MULTI-CHART
      */
      const multiChartData = resp.multiBarChartResponse.todayMultiBarChart.list.map(item => ({
        name: item.chartName,
        color: item.chartColor,
        data: item.data,
      }));

      this.multiBarChartModel.labels = resp.multiBarChartResponse.todayMultiBarChart.labels;
      this.multiBarChartModel.list = multiChartData;

      // pieChart
      const pieChartData = resp.pieChartResponse.todayPieChart.list.map(item => ({
        name: item.chartName,
        value: item.data,
        color: item.chartColor
      }));

      this.pieChartModel.list = pieChartData;
    });
  }

  setFilterTo(filter: string) {
    let lineChartModel: LineChartModel = {
      list: [], xValues: []
    };

    let radarChartModel: BudgetChartModel = {
      indicators: [],
      list: []
    };

    let multiBarModel: MultiBarModel = {
      labels: [], list: []
    };

    let pieChartModel: PieChartModel = {
      list: []
    };

    let selectedDashboardSummaryResponse: DashboardSummaryItem = {
      totalLose: 0, totalProfit: 0, totalWins: 0
    };

    let selectedLineChartResponse: GetLineChartResponse = {
      labels: [], list: []
    };
    let selectedRadarChartResponse: GetRadarChartResponse = {
      labels: [], list: [], maxValue: 0
    };

    let selectedMultiBarChartResponse: MultiBarChartResponse = {
      labels: [], list: []
    };

    let selectedPieChartModel: PieChartResponse = {
      labels: [], list: []
    };

    if (filter == 'TODAY') {
      this.selectedFilterDisplay = 'Today';
      selectedDashboardSummaryResponse = this.getDashboardOverviewResponse.dashboardSummary.todaySummary;
      selectedLineChartResponse = this.getDashboardOverviewResponse.lineChartResponse.todayLineChart;
      selectedRadarChartResponse = this.getDashboardOverviewResponse.radarChartResponse.todayRadarChart;
      selectedMultiBarChartResponse = this.getDashboardOverviewResponse.multiBarChartResponse.todayMultiBarChart;
      selectedPieChartModel = this.getDashboardOverviewResponse.pieChartResponse.todayPieChart;
    } else if (filter == 'WEEK') {
      this.selectedFilterDisplay = 'This Week';
      selectedDashboardSummaryResponse = this.getDashboardOverviewResponse.dashboardSummary.thisWeekSummary;
      selectedLineChartResponse = this.getDashboardOverviewResponse.lineChartResponse.thisWeekLineChart;
      selectedRadarChartResponse = this.getDashboardOverviewResponse.radarChartResponse.thisWeekRadarChart;
      selectedMultiBarChartResponse = this.getDashboardOverviewResponse.multiBarChartResponse.thisWeekMultiBarChart;
      selectedPieChartModel = this.getDashboardOverviewResponse.pieChartResponse.thisWeekPieChart;
    } else if (filter == 'MONTH') {
      this.selectedFilterDisplay = 'This Month';
      selectedDashboardSummaryResponse = this.getDashboardOverviewResponse.dashboardSummary.thisMonthSummary;
      selectedLineChartResponse = this.getDashboardOverviewResponse.lineChartResponse.thisMonthLineChart;
      selectedRadarChartResponse = this.getDashboardOverviewResponse.radarChartResponse.thisMonthRadarChart;
      selectedMultiBarChartResponse = this.getDashboardOverviewResponse.multiBarChartResponse.thisMonthMultiBarChart;
      selectedPieChartModel = this.getDashboardOverviewResponse.pieChartResponse.thisMonthPieChart;
    }

    /*
     * SUMMARY
     */
    this.dashboardSummaryResponse = selectedDashboardSummaryResponse;

    /*
    * LINE CHART
    */
    lineChartModel.xValues = selectedLineChartResponse.labels;
    lineChartModel.list = selectedLineChartResponse.list.map(item => ({
      label: item.chartName,
      color: item.chartColor,
      yValues: item.data
    }));
    this.lineChartModel = lineChartModel;

    /*
    * RADAR CHART
    */
    radarChartModel.indicators = selectedRadarChartResponse.labels;
    radarChartModel.list = selectedRadarChartResponse.list.map(item => ({
      label: item.chartName,
      color: item.chartColor,
      data: item.data
    }));
    this.radarChartModel = radarChartModel;

    /*
    * MULTI-CHART
    */
    multiBarModel.labels = selectedMultiBarChartResponse.labels;
    multiBarModel.list = selectedMultiBarChartResponse.list.map(item => ({
      name: item.chartName,
      color: item.chartColor,
      data: item.data,
    }));
    this.multiBarChartModel = multiBarModel;

    /*
    * PIE-CHART
    */
    pieChartModel.list = selectedPieChartModel.list.map(item => ({
      name: item.chartName,
      value: item.data,
      color: item.chartColor
    }));

    this.pieChartModel = pieChartModel;
  }
}
