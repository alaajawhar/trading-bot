import {AfterViewInit, Component, OnInit} from '@angular/core';
import {BudgetChartModel} from "../../shared/components/charts/budget-chart/models";
import {TrafficChartModel} from "../../shared/components/charts/traffic-chart/models";
import {LineChartModel} from "../../shared/components/charts/line-chart/models";
import {Backend} from "../../shared/services/backend";
import {Router} from "@angular/router";
import {DashboardSummaryResponse} from "../../shared/services/payloads";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {

  dashboardSummaryResponse: DashboardSummaryResponse = {
    totalLose: 0, totalProfit: 0, totalWins: 0
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
  * Traffic Chart
  */
  trafficChartData: TrafficChartModel = {
    subTitle: 'Result of',
    list: [
      {
        label: 'Bot1',
        percentage: 40
      },
      {
        label: 'Bot2',
        percentage: 30
      },
      {
        label: 'Bot3',
        percentage: 30
      }]
  };


  constructor(private backend: Backend, private router: Router) {
  }

  ngOnInit(): void {
    this.backend.getDashboardSummary({filter: 'TODAY'}).subscribe(resp => {
      this.dashboardSummaryResponse = resp;
    });

    this.backend.getDashboardStrategiesPerformanceOverTime({filter: 'TODAY'}).subscribe(resp => {
      this.lineChartData.xValues = resp.labels;

      const data = resp.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        yValues: item.data
      }));

      this.lineChartData.list = data;
    });

    this.backend.getDashboardStrategiesPerformanceBaseOnTimeframe({filter: 'TODAY'}).subscribe(resp => {
      this.budgetChartData.indicators = resp.labels;

      const data = resp.list.map(item => ({
        label: item.chartName,
        color: item.chartColor,
        data: item.data
      }));

      this.budgetChartData.list = data;
    });
  }

  ngAfterViewInit(): void {
  }
}
