import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {DropdownResponse, GetSignalByIdResponse, GetSignalListRequest, GetSignalListResponse} from "./payloads";

@Injectable()
export class Backend {

  constructor(private http: HttpClient) {
  }

  public getSignalList(request: GetSignalListRequest) {
    return this.http.post<GetSignalListResponse>(environment.backendUrl + "/signal/list", request);
  }

  public GetSignalById(id: string) {
    return this.http.get<GetSignalByIdResponse>(environment.backendUrl + "/signal/" + id);
  }

  public getDropdownBots() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/bots");
  }


  public getDropdownOutcomes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/outcomes");
  }

  public getDropdownTimeframes() {
    return this.http.get<DropdownResponse>(environment.backendUrl + "/dropdown/timeframes");
  }


}
