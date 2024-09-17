import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {DropdownResponse, GetSignalByIdResponse, GetSignalListRequest, GetSignalListResponse} from "./payloads";

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

  public getDropdownBots() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/bots", {headers: this.headers});
  }


  public getDropdownOutcomes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/outcomes", {headers: this.headers});
  }

  public getDropdownTimeframes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/timeframes", {headers: this.headers});
  }


}
