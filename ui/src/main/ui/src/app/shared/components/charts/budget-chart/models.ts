export interface BudgetChartModel {
    indicators: string[];
    list: BudgetChartItem[];
}

export interface BudgetChartItem {
    label: string;
    color: string;
    data: number[];
}
