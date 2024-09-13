import {Component, ElementRef} from '@angular/core';
import {Router} from '@angular/router';
import {AppRoutingModule} from "./app-routing.module";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Trading Bot';

  appHeaderExceptions: string[] = AppRoutingModule.appHeaderExceptions;

  appSideBarExceptions: string[] = AppRoutingModule.appSideBarExceptions;

  appFooterExceptions: string[] = AppRoutingModule.appFooterExceptions;

  constructor(private elementRef: ElementRef, public _router: Router) {
  }

  ngOnInit() {
    // var s = document.createElement("script");
    // s.type = "text/javascript";
    // s.src = "../assets/js/main.js";
    // this.elementRef.nativeElement.appendChild(s);
  }
}
