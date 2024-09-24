import {Component, Input, OnInit} from '@angular/core';
import {DisabledFieldsCard} from './models';

@Component({
  selector: 'app-disabled-field-card',
  templateUrl: './disabled-field-card.component.html',
  styleUrls: ['./disabled-field-card.component.css']
})
export class DisabledFieldCardComponent implements OnInit {
  @Input() cardTitle: string;
  @Input() disabledFieldsCard: DisabledFieldsCard = {
    list: []
  };

  constructor() {
  }

  ngOnInit(): void {
  }

}
