import {Component, OnInit} from '@angular/core';
import {Backend} from "../../shared/services/backend";
import {
  DropdownItem,
  DropdownResponse,
  GetSignalListRequest,
  GetSignalListResponse
} from "../../shared/services/payloads";
import {Router} from "@angular/router";
import {getDropdownValueFromKey} from "../../shared/utils/function.utils";

@Component({
  selector: 'app-signals',
  templateUrl: './signals.component.html',
  styleUrls: ['./signals.component.css']
})
export class SignalsComponent implements OnInit {
  itemsPerPage: number = 15;
  selectedPageNumber: number = 0;
  isLoadingData: boolean = true;

  dropdownBots: DropdownResponse = {
    list: []
  };
  dropdownTimeframes: DropdownResponse = {
    list: []
  };
  dropdownOutcomes: DropdownResponse = {
    list: []
  };
  signalListResponse: GetSignalListResponse = {
    list: [], offset: 0, totalCount: 0
  };


  getSignalListRequest: GetSignalListRequest = {
    strategyId: undefined!,
    symbolId: undefined!,
    detectionId: undefined!,
    fromDate: undefined!,
    toDate: undefined!,
    outcomeResult: undefined!,
    timeFrame: undefined!,
    limit: this.itemsPerPage,
    offset: 0
  };

  constructor(private backend: Backend, private router: Router) {
  }

  ngOnInit(): void {
    this.isLoadingData = true;
    this.backend.getSignalList(this.getSignalListRequest).subscribe(resp => {
      this.signalListResponse = resp;
      this.isLoadingData = false;
    })

    this.backend.getDropdownStrategies().subscribe(resp => {
      this.dropdownBots = resp;
    })

    this.backend.getDropdownOutcomes().subscribe(resp => {
      this.dropdownOutcomes = resp;
    })

    this.backend.getDropdownTimeframes().subscribe(resp => {
      this.dropdownTimeframes = resp;
    })
  }

  onFilter() {
    this.isLoadingData = true;
    this.resetPagination();
    this.getSignalListRequest.offset = this.selectedPageNumber * this.itemsPerPage;
    this.fetchData();
  }

  fetchData() {
    this.getSignalListRequest.offset = this.selectedPageNumber * this.itemsPerPage;

    const toSendRequest: GetSignalListRequest = {
      strategyId: this.getSignalListRequest.strategyId,
      symbolId: this.getSignalListRequest.symbolId,
      detectionId: this.getSignalListRequest.detectionId?.length == 0 ? undefined! : this.getSignalListRequest.detectionId?.trim(),
      fromDate: this.getSignalListRequest.fromDate == undefined ? undefined! : new Date(this.getSignalListRequest.fromDate).getTime(),
      toDate: this.getSignalListRequest.toDate == undefined ? undefined! : new Date(this.getSignalListRequest.toDate).getTime(),
      outcomeResult: this.getSignalListRequest.outcomeResult,
      timeFrame: this.getSignalListRequest.timeFrame,
      limit: this.itemsPerPage,
      offset: this.selectedPageNumber * this.itemsPerPage
    };

    this.backend.getSignalList(toSendRequest).subscribe(resp => {
      this.signalListResponse = resp;
      this.isLoadingData = false;
    })
  }

  onPrevious() {
    this.selectedPageNumber--;
    this.fetchData();
  }

  onNext() {
    this.selectedPageNumber++;
    this.fetchData();
  }

  onSignalClick(detectionId: string) {
    this.router.navigate(['/signals', detectionId]);
  }

  getValueById = (dropdowns: DropdownItem[], id: string) => getDropdownValueFromKey(dropdowns, id);

  resetPagination() {
    this.selectedPageNumber = 0;
  }

  onFilterReset() {
    this.getSignalListRequest = {
      strategyId: undefined!,
      symbolId: undefined!,
      detectionId: undefined!,
      fromDate: undefined!,
      toDate: undefined!,
      outcomeResult: undefined!,
      timeFrame: undefined!,
      limit: this.itemsPerPage,
      offset: 0
    };
  }
}
