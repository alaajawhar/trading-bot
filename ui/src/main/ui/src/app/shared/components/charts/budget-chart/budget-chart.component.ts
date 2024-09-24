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
      this.injectLineChartGraph();
    }, 1000);
  }

  injectLineChartGraph() {
    let echarts = (window as any).echarts;  // Access ECharts from the global window object
    const chartDom = document.getElementById('budgetChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #budgetChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.budgetChart = echarts.init(chartDom);

    const seriesData = this.data.list.map(item => ({
      name: item.label,
      value: item.data
    }));

    const indicatorsData = this.data.indicators.map(item => ({
      name: item,
      max: this.data.maxValue
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
