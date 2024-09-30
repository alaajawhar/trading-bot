import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {PieChartModel} from "./pie-chart.models";

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.css']
})
export class PieChartComponent implements OnInit {

  @Input() data: PieChartModel;

  pieChart: any;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    setTimeout(() => {
      this.injectPieChartGraph();
    }, 1000);
  }

  injectPieChartGraph() {
    let echarts = (window as any).echarts;  // Access ECharts from the global window object
    const chartDom = document.getElementById('pieChart') as HTMLElement;

    if (!echarts || !chartDom) {
      console.error('ECharts or #pieChart element not found!');
      return;
    }

    // Initialize the ECharts instance
    this.pieChart = echarts.init(chartDom);

    const optionsData = this.data.list.map(item => ({
      name: item.name,
      value: item.value,
      itemStyle: {color: item.color}
    }));

    // Set up the options for the pie chart
    const option = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: this.data.itemDescription,
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 20,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: optionsData
        }
      ]
    };

    // Apply the options to the ECharts instance
    this.pieChart.setOption(option);
  }
}
