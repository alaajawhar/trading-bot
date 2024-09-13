import {Component, OnInit} from '@angular/core';
import {AppConstants} from "../../app.constants";
import {Router} from "@angular/router";

@Component({
  selector: 'app-pages-login',
  templateUrl: './pages-login.component.html',
  styleUrls: ['./pages-login.component.css']
})
export class PagesLoginComponent implements OnInit {

  /* CONFIG */
  isShowRememberMe: boolean = true;
  isShowCreateAccount: boolean = true;
  registerRouting: string = '/register';

  /* INTERNAL */
  ownerCompany: string = AppConstants.OWNER_COMPANY;
  ownerCompanyUrl: string = AppConstants.OWNER_COMPANY_URL;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  onLoginClick() {
    this.router.navigate(['/requirements'])
  }
}
