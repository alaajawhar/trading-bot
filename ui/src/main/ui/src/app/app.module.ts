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
import {BsModalService, ModalModule} from "ngx-bootstrap/modal";
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
import {DashboardComponent} from "./modules/dashboard/dashboard.component";
import {LineChartComponent} from './shared/components/charts/line-chart/line-chart.component';
import {BudgetChartComponent} from './shared/components/charts/budget-chart/budget-chart.component';
import {StrategiesTestComponent} from "./modules/test/strategies-test/strategies-test.component";
import {PieChartComponent} from './shared/components/charts/pie-chart/pie-chart.component';
import {MultiBarChartComponent} from './shared/components/charts/multi-bar-chart/multi-bar-chart.component';
import {DialogService} from "./shared/services/dialogService/dialog.service";

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
    DashboardComponent,
    SignalDetailsComponent,
    DisabledFieldCardComponent,
    CustomDateFormatPipe,
    LineChartComponent,
    BudgetChartComponent,
    StrategiesTestComponent,
    PieChartComponent,
    MultiBarChartComponent
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
    ModalModule.forRoot(),
  ],
  providers: [
    BsModalService,
    ComponentLoaderFactory,
    PositioningService,
    HttpClientModule,
    Backend,
    DialogService
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
