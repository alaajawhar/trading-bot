import {Component, Input, OnInit} from '@angular/core';
import {TrafficChartModel} from "./models";

@Component({
  selector: 'app-traffic-chart',
  templateUrl: './traffic-chart.component.html',
  styleUrls: ['./traffic-chart.component.css']
})
export class TrafficChartComponent implements OnInit {
  @Input() data: TrafficChartModel;

  trafficChart: any;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.injectTrafficChartGraph();
    }, 1);
  }

  injectTrafficChartGraph<T>() {
    let echarts = (window as any).echarts;  // Access ECharts from the global window object
    const chartDom = document.getElementById('trafficChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #trafficChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.trafficChart = echarts.init(chartDom);

    const percentageData = this.data.list.map(item => ({
      name: item.label,
      value: item.percentage
    }));


    // Set up the options for the radar chart
    const options = {
      tooltip: {trigger: 'item'},
      legend: {top: '5%', left: 'center'},
      series: [{
        name: this.data.subTitle,
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {show: false, position: 'center'},
        emphasis: {label: {show: true, fontSize: '18', fontWeight: 'bold'}},
        labelLine: {show: false},
        data: percentageData
      }]
    };

    // Apply the options to the ECharts instance
    this.trafficChart.setOption(options);
  }

}
