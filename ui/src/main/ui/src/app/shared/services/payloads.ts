export interface GetSignalListRequest {
  detectionId: string;
  timeFrame: string;
  strategyId: number;
  symbolId: number;
  fromDate: Date;
  toDate: Date;
  outcomeResult: string;
  offset: number;
  limit: number;
}

export interface GetSignalListResponse {
  list: SignalItem[];
  offset: number;
  totalCount: number;
}

export interface SignalItem {
  detectionId: string;
  botId: string;
  timeFrame: string;
  outcomeResult: string;
  profit: number;
  date: Date;
}

export interface GetSignalByIdResponse {
  detectionId: string;
  botId: string;
  timeframe: string;
  metaData: object;
  tradeList: TradeDetailsItem[]
}

export interface DropdownResponse {
  list: DropdownItem[]
}

export interface DropdownItem {
  id: string;
  value: string;
}

export interface TradeDetailsItem {
  signalId: number;
  tradeAction: string;
  trades: TradeItem[]
}

export interface TradeItem {
  tradeId: number;
  status: string;
  brokerRequest: string;
  brokerResponse: string;
  brokerError: string;
  date: Date;
}

export interface DashboardSummaryRequest {

}

export interface DashboardSummaryResponse {
  totalWins: number;
  totalProfit: number;
  totalLose: number;
}

export interface DashboardBotPerformanceRequest {

}

export interface DashboardStrategiesPerformanceOverPeriodResponse {
  labels: string[];
  list: DashboardStrategiesPerformanceOverPeriodResponseItem[]
}

export interface DashboardStrategiesPerformanceOverPeriodResponseItem {
  chartName: string;
  chartColor: string;
  data: number[]
}

export interface DashboardStrategiesPerformanceOverTimeframeResponse {
  labels: string[];
  maxValue: number;
  list: DashboardBotPerformanceOverTimeframeResponseItem[]
}

export interface DashboardBotPerformanceOverTimeframeResponseItem {
  chartName: string;
  chartColor: string;
  data: number[]
}

export interface PieChartResponse {
  labels: string[];
  list: PieChartResponseItem[]
}

export interface PieChartResponseItem {
  chartName: string;
  chartColor: string;
  data: number
}

export interface MultiBarChartResponse {
  labels: string[];
  list: MultiBarChartResponseItem[]
}

export interface MultiBarChartResponseItem {
  chartName: string;
  chartColor: string;
  data: number[]
}

export interface StrategyTestResponse {
  summaryResponse: DashboardSummaryResponse;
  performanceBaseOnTimeframesResponse: DashboardStrategiesPerformanceOverTimeframeResponse;
  performanceOverTimeResponse: DashboardStrategiesPerformanceOverPeriodResponse;
  pieChartResponse: PieChartResponse;
  multiBarChartResponse: MultiBarChartResponse;
  signals: GetSignalTestListResponse;
}

export interface StrategyTestRequest {
  strategyId: number;
  symbolId: number;
}

export interface GetSignalTestListResponse {
  list: SignalTestItem[];
}

export interface SignalTestItem {
  timeframe: string;
  outcomeResult: string;
  profit: string;
  metaData: object;
  date: Date;
}


