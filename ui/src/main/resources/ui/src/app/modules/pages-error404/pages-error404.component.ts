import {Component, OnInit} from '@angular/core';
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'app-pages-error404',
  templateUrl: './pages-error404.component.html',
  styleUrls: ['./pages-error404.component.css']
})
export class PagesError404Component implements OnInit {
  ownerCompany: string = AppConstants.OWNER_COMPANY;
  ownerCompanyUrl: string = AppConstants.OWNER_COMPANY_URL;

  constructor() {
  }

  ngOnInit(): void {
  }

}
