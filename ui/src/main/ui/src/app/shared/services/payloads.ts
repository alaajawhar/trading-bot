export interface GetSignalListRequest {
  detectionId: string;
  timeFrame: string;
  strategyId: number;
  symbolId: number;
  fromDate: number;
  toDate: number;
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

export interface DashboardSummaryItem {
  totalWins: number;
  totalProfit: number;
  totalLose: number;
}

export interface GetLineChartResponse {
  labels: string[];
  list: LineChartResponseItem[]
}

export interface LineChartResponseItem {
  chartName: string;
  chartColor: string;
  data: number[]
}

export interface GetRadarChartResponse {
  labels: string[];
  maxValue: number;
  list: RadarChartResponseItem[]
}

export interface RadarChartResponseItem {
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

export interface GetDashboardSummaryResponse {
  todaySummary: DashboardSummaryItem;
  thisWeekSummary: DashboardSummaryItem;
  thisMonthSummary: DashboardSummaryItem;
}

export interface GetDashboardLineChartResponse {
  todayLineChart: GetLineChartResponse;
  thisWeekLineChart: GetLineChartResponse;
  thisMonthLineChart: GetLineChartResponse;
}

export interface GetDashboardMultiBarChartResponse {
  todayMultiBarChart: MultiBarChartResponse;
  thisWeekMultiBarChart: MultiBarChartResponse;
  thisMonthMultiBarChart: MultiBarChartResponse;
}

export interface GetDashboardRadarResponse {
  todayRadarChart: GetRadarChartResponse;
  thisWeekRadarChart: GetRadarChartResponse;
  thisMonthRadarChart: GetRadarChartResponse;
}

export interface GetDashboardPieChartResponse {
  todayPieChart: PieChartResponse;
  thisWeekPieChart: PieChartResponse;
  thisMonthPieChart: PieChartResponse;
}

export interface GetDashboardOverviewResponse {
  dashboardSummary: GetDashboardSummaryResponse;
  lineChartResponse: GetDashboardLineChartResponse;
  radarChartResponse: GetDashboardRadarResponse;
  multiBarChartResponse: GetDashboardMultiBarChartResponse;
  pieChartResponse: GetDashboardPieChartResponse;
}

export interface StrategyTestResponse {
  summaryResponse: DashboardSummaryItem;
  performanceBaseOnTimeframesResponse: GetRadarChartResponse;
  performanceOverTimeResponse: GetLineChartResponse;
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


