export interface LineChartModel {
    xValues: string[];
    list: LineChartModelItem[];
}

export interface LineChartModelItem {
    label: string;
    color: string;
    yValues: number[];
}
