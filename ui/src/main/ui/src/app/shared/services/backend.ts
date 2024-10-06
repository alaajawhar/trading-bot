import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {
  DropdownResponse,
  GetDashboardOverviewResponse,
  GetSignalByIdResponse,
  GetSignalListRequest,
  GetSignalListResponse,
  StrategyTestRequest,
  StrategyTestResponse
} from "./payloads";
import {environment} from "../../../environments/environment";

@Injectable()
export class Backend {

  headers: HttpHeaders = new HttpHeaders({ // TODO: delete
    'ngrok-skip-browser-warning': 'xyz',
  });

  constructor(private http: HttpClient) {
  }

  public getSignalList(request: GetSignalListRequest) {
    return this.http.post<GetSignalListResponse>(this.getBaseUrl() + "/signal/list", request, {headers: this.headers});
  }

  public getSignalById(id: string) {
    return this.http.get<GetSignalByIdResponse>(this.getBaseUrl() + "/signal/" + id, {headers: this.headers});
  }

  public getDropdownStrategies() {
    return this.http.get<DropdownResponse>(this.getBaseUrl() + "/dropdown/strategies", {headers: this.headers});
  }


  public getDropdownOutcomes() {
    return this.http.get<DropdownResponse>(this.getBaseUrl() + "/dropdown/outcomes", {headers: this.headers});
  }

  public getDropdownSymbols() {
    return this.http.get<DropdownResponse>(this.getBaseUrl() + "/dropdown/symbols", {headers: this.headers});
  }

  public getDropdownTimeframes() {
    return this.http.get<DropdownResponse>(this.getBaseUrl() + "/dropdown/timeframes", {headers: this.headers});
  }

  public getDashboardOverview() {
    return this.http.post<GetDashboardOverviewResponse>(this.getBaseUrl() + "/dashboard/overview", {headers: this.headers});
  }

  public getTestStrategy(request: StrategyTestRequest) {
    return this.http.post<StrategyTestResponse>(this.getBaseUrl() + "/strategy/test/summary", request, {headers: this.headers});
  }

  private getBaseUrl(): string {
    let baseUrl: string = environment.backendUrl;
    const regex = /^(http?:\/\/[^\/?#]+)(?:[\/?#]|$)/i;
    const match = baseUrl.match(regex);

    if (!match) {
      console.error('url not matched')
      return 'not-found';
    }

    return match[1];
  }
}
