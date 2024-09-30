import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {MultiBarModel} from "./multi-bar-chart.models";

@Component({
  selector: 'app-multi-bar-chart',
  templateUrl: './multi-bar-chart.component.html',
  styleUrls: ['./multi-bar-chart.component.css']
})
export class MultiBarChartComponent implements OnInit {

  @Input() data: MultiBarModel;

  multiBarChart: any;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    setTimeout(() => {
      this.injectMultiBarChartGraph();
    }, 1000);
  }

  injectMultiBarChartGraph() {
    let echarts = (window as any).echarts;
    const chartDom = document.getElementById('multiBarChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #multiBarChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.multiBarChart = echarts.init(chartDom);

    const dataSeries = this.data.list.map(item => ({
      name: item.name,
      type: 'bar',
      emphasis: {
        focus: 'series'
      },
      data: item.data,
      itemStyle: {
        color: item.color  // Function to get specific colors for each bar
      }
    }));

    // Set up the options for the pie chart
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {},
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          data: this.data.labels
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: dataSeries
    };

    // Apply the options to the ECharts instance
    this.multiBarChart.setOption(option);
  }
}
