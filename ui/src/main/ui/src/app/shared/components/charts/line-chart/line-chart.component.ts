import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {LineChartModel} from "./models";

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit, OnChanges {
  @Input() data: LineChartModel

  lineChart: any;

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
    const chartDom = document.getElementById('lineChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #lineChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.lineChart = echarts.init(chartDom);

    const dataSeries = this.data.list.map(item => ({
      name: item.label,
      data: item.yValues,
      type: 'line',
      smooth: true,
      lineStyle: {
        color: item.color  // Set the color for the line
      },
      itemStyle: {
        color: item.color  // Set the color for the data points (markers)
      },
    }));

    const legends = this.data.list.map(item => item.label);

    // Set up the options for the lineChart chart
    const options = {
      grid: {
        left: '2%',
        right: '3%',
        top: '10%',
        bottom: '3%',
        containLabel: true  // Ensure labels are not cut off
      },
      legend: {
        data: legends,
        icon: 'roundRect',// Automatically use the series names for the legend
      },
      xAxis: {
        type: 'category',
        data: this.data.xValues,  // Assuming you have values from '00:00' to '23:00'
        axisLabel: {
          interval: 0,  // Show all labels
          rotate: 45,  // Optional: Rotate labels for better readability if they overlap
          formatter: function (value: string) {
            return value;
          },
          textStyle: {
            color: '#666',  // Optional: Customize label color
            fontSize: 10  // Optional: Customize font size
          }
        },
        axisTick: {
          alignWithLabel: true  // Align ticks with the labels
        },
        boundaryGap: false  // Ensure the chart starts from the first label
      },
      yAxis: {
        type: 'value'
      },
      tooltip: {
        trigger: 'axis',  // Show tooltips when hovering over the axis
        axisPointer: {
          type: 'line'  // Display a line indicator
        },
        formatter: function (params: any) {
          const [param] = params;
          return `${param.marker} ${param.seriesName}: ${param.value}`;
        }
      },
      series: dataSeries
    };

    // Apply the options to the ECharts instance
    this.lineChart.setOption(options);
  }

}
