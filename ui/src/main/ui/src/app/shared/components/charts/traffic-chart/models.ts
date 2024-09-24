export interface TrafficChartModel {
    subTitle: string;
    list: TrafficChartItem[];
}

export interface TrafficChartItem {
    label: string;
    percentage: number;
}
