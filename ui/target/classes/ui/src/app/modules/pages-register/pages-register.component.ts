import {Component, OnInit} from '@angular/core';
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'app-pages-register',
  templateUrl: './pages-register.component.html',
  styleUrls: ['./pages-register.component.css']
})
export class PagesRegisterComponent implements OnInit {

  /* CONFIG */
  loginRouting: string = '/login';


  /* INTERNAL */
  ownerCompany: string = AppConstants.OWNER_COMPANY;
  ownerCompanyUrl: string = AppConstants.OWNER_COMPANY_URL;

  constructor() { }

  ngOnInit(): void {
  }

}
