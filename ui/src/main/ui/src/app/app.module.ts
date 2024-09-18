import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './layouts/header/header.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {SidebarComponent} from './layouts/sidebar/sidebar.component';
import {SimpleNotificationsModule} from "angular2-notifications";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CollapseModule} from "ngx-bootstrap/collapse";
import {BsModalService} from "ngx-bootstrap/modal";
import {ComponentLoaderFactory} from "ngx-bootstrap/component-loader";
import {PositioningService} from "ngx-bootstrap/positioning";
import {FormsModule} from "@angular/forms";
import {AngularMultiSelectModule} from "angular2-multiselect-dropdown";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {PagesError404Component} from "./modules/pages-error404/pages-error404.component";
import {UsersProfileComponent} from "./modules/users-profile/users-profile.component";
import {PagesLoginComponent} from "./modules/pages-login/pages-login.component";
import {PagesRegisterComponent} from "./modules/pages-register/pages-register.component";
import {SignalsComponent} from './modules/signals/signals.component';
import {Backend} from "./shared/services/backend";
import {HttpClientModule} from "@angular/common/http";
import {SignalDetailsComponent} from './modules/signals/signal-details/signal-details.component';
import {DisabledFieldCardComponent} from './shared/components/disabled-field-card/disabled-field-card.component';
import {CustomDateFormatPipe} from './shared/pipes/custom-date-format.pipe';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    PagesError404Component,
    UsersProfileComponent,
    PagesLoginComponent,
    PagesRegisterComponent,
    SignalsComponent,
    SignalDetailsComponent,
    DisabledFieldCardComponent,
    CustomDateFormatPipe,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SimpleNotificationsModule.forRoot(),
    CollapseModule,
    FormsModule,
    AngularMultiSelectModule,
    DragDropModule,
    HttpClientModule,
  ],
  providers: [
    BsModalService,
    ComponentLoaderFactory,
    PositioningService,
    HttpClientModule,
    Backend,
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
