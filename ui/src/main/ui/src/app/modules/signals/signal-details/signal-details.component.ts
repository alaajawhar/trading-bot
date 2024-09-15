import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Backend} from "../../../shared/services/backend";
import {DropdownResponse, GetSignalByIdResponse} from "../../../shared/services/payloads";
import {DisabledFieldItem, DisabledFieldsCard} from "../../../shared/components/disabled-field-card/models";

@Component({
  selector: 'app-signal-details',
  templateUrl: './signal-details.component.html',
  styleUrls: ['./signal-details.component.css']
})
export class SignalDetailsComponent implements OnInit {
  private isLoadingData: boolean = true;
  // DTO
  basicInformationData: DisabledFieldsCard = undefined!
  responseMetaData: DisabledFieldsCard = undefined!

  // BACKEND RESPONSE
  detectionId: string = undefined!;
  signalByIdResponse: GetSignalByIdResponse;
  dropdownTimeframes: DropdownResponse = undefined!;


  constructor(private backend: Backend, private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params => {
      this.detectionId = params.get('detectionId')!;
    });
  }

  ngOnInit(): void {
    this.backend.getSignalById(this.detectionId).subscribe(resp => {
      this.signalByIdResponse = resp;
      this.isLoadingData = false;
      this.basicInformationData = {
        list: [
          {label: 'Detection Id', value: resp.detectionId},
          {label: 'Bot Name', value: resp.botId},
          {label: 'Timeframe', value: resp.timeframe}
        ]
      };

      this.responseMetaData = this.convertResponseToDisabledFieldsCard(resp.metaData);
    })

    this.backend.getDropdownTimeframes().subscribe(resp => {
      this.dropdownTimeframes = resp;
    })
  }


// Function to convert the response object into DisabledFieldsCard format
  convertResponseToDisabledFieldsCard(response: any): DisabledFieldsCard {
    const disabledFieldsList: DisabledFieldItem[] = [];

    // Loop through each key-value pair in the response
    for (const key in response) {
      if (response.hasOwnProperty(key)) {
        const label = this.convertCamelCaseToLabel(key);  // Convert field name to label
        const value = String(response[key]);         // Get the field value as a string
        disabledFieldsList.push({label, value});   // Add to the list
      }
    }

    // Return the DisabledFieldsCard object
    return {
      list: disabledFieldsList
    };
  }

  convertCamelCaseToLabel(fieldName: string): string {
    return fieldName
      .replace(/([A-Z])/g, ' $1')  // Insert space before capital letters
      .replace(/^./, str => str.toUpperCase());  // Capitalize the first letter
  }

}
