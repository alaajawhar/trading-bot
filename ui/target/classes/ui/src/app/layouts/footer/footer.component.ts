import {Component, OnInit} from '@angular/core';
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  productOwner: string = AppConstants.OWNER_COMPANY;
  constructor() { }

  ngOnInit(): void {
  }
  scrollTop()
  {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
});

  }
}
