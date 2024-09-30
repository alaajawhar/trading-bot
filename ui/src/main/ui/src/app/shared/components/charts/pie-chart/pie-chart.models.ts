export interface PieChartModel {
  itemDescription?: string;
  list: PieChartItem[];
}

export interface PieChartItem {
  name: string;
  value: number;
  color: string;
}
