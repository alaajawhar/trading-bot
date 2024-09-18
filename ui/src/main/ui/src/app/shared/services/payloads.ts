export interface GetSignalListRequest {
  detectionId: string;
  timeFrame: string;
  botId: number;
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
