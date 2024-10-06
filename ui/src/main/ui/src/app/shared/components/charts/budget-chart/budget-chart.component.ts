import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {BudgetChartModel} from "./models";

@Component({
  selector: 'app-budget-chart',
  templateUrl: './budget-chart.component.html',
  styleUrls: ['./budget-chart.component.css']
})
export class BudgetChartComponent implements OnInit, OnChanges {
  @Input() data: BudgetChartModel;

  budgetChart: any;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    setTimeout(() => {
      this.injectRadarChartGraph();
    }, 1000);
  }

  injectRadarChartGraph() {
    let echarts = (window as any).echarts;  // Access ECharts from the global window object
    const chartDom = document.getElementById('radarChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #radarChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.budgetChart = echarts.init(chartDom);

    const seriesData = this.data.list.map(item => ({
      name: item.label,
      value: item.data,
      lineStyle: {
        width: 2,         // Set the line thickness
        type: 'solid',    // Can be 'solid', 'dashed', 'dotted'
        color: item.color // Set the color for the line
      },
      itemStyle: {
        color: item.color,  // Color for the dots and the line
      },
    }));

    const maxValue = Math.abs(Math.max(...this.data.list.flatMap(item => item.data)));
    const minValue = Math.abs(Math.min(...this.data.list.flatMap(item => item.data)));


    const indicatorsData = this.data.indicators.map(item => ({
      name: item,
      max: Math.max(maxValue, minValue)
    }));

    // Set up the options for the radar chart
    const options = {
      legend: {
        data: this.data.list.map(item => item.label)
      },
      tooltip: {
        trigger: 'item' // This enables the tooltip to show the values when hovering over
      },
      radar: {
        indicator: indicatorsData
      },
      series: [{
        type: 'radar',
        data: seriesData
      }]
    };

    // Apply the options to the ECharts instance
    this.budgetChart.setOption(options);
  }

}
