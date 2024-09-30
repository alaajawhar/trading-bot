import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {
  DashboardBotPerformanceRequest,
  DashboardStrategiesPerformanceOverPeriodResponse,
  DashboardStrategiesPerformanceOverTimeframeResponse,
  DashboardSummaryRequest,
  DashboardSummaryResponse,
  DropdownResponse,
  GetSignalByIdResponse,
  GetSignalListRequest,
  GetSignalListResponse,
  StrategyTestRequest,
  StrategyTestResponse
} from "./payloads";

@Injectable()
export class Backend {

  headers: HttpHeaders = new HttpHeaders({ // TODO: delete
    'ngrok-skip-browser-warning': 'xyz',
  });

  constructor(private http: HttpClient) {
  }

  public getSignalList(request: GetSignalListRequest) {
    return this.http.post<GetSignalListResponse>(environment.backendUrl + "/signal/list", request, {headers: this.headers});
  }

  public getSignalById(id: string) {
    return this.http.get<GetSignalByIdResponse>(environment.backendUrl + "/signal/" + id, {headers: this.headers});
  }

  public getDropdownStrategies() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/strategies", {headers: this.headers});
  }


  public getDropdownOutcomes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/outcomes", {headers: this.headers});
  }

  public getDropdownSymbols() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/symbols", {headers: this.headers});
  }

  public getDropdownTimeframes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/timeframes", {headers: this.headers});
  }

  public getDashboardSummary(request: DashboardSummaryRequest) {
    return this.http.post<DashboardSummaryResponse>(environment.backendUrl + "/dashboard/summary", request, {headers: this.headers});
  }

  public getDashboardStrategiesPerformanceOverTime(request: DashboardBotPerformanceRequest) {
    return this.http.post<DashboardStrategiesPerformanceOverPeriodResponse>(environment.backendUrl + "/dashboard/strategies/performance/period", request, {headers: this.headers});
  }

  public getDashboardStrategiesPerformanceBaseOnTimeframe(request: DashboardBotPerformanceRequest) {
    return this.http.post<DashboardStrategiesPerformanceOverTimeframeResponse>(environment.backendUrl + "/dashboard/strategies/performance/timeframes", request, {headers: this.headers});
  }

  public getTestStrategy(request: StrategyTestRequest) {
    return this.http.post<StrategyTestResponse>(environment.backendUrl + "/strategy/test/summary", request, {headers: this.headers});
  }
}
