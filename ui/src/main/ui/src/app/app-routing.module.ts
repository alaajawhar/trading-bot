import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PagesError404Component} from "./modules/pages-error404/pages-error404.component";
import {UsersProfileComponent} from "./modules/users-profile/users-profile.component";
import {PagesRegisterComponent} from "./modules/pages-register/pages-register.component";
import {PagesLoginComponent} from "./modules/pages-login/pages-login.component";
import {SignalsComponent} from "./modules/signals/signals.component";
import {SignalDetailsComponent} from "./modules/signals/signal-details/signal-details.component";
import {DashboardComponent} from "./modules/dashboard/dashboard.component";

const routes: Routes = [
  {path: '', component: DashboardComponent},
  {path: 'signals', component: SignalsComponent},
  {path: 'signals/:detectionId', component: SignalDetailsComponent},
  {path: 'profile', component: UsersProfileComponent},
  {path: 'login', component: PagesLoginComponent},
  {path: 'register', component: PagesRegisterComponent},
  {path: 'error404', component: PagesError404Component, data: {fullScreen: true}},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

  static commonExceptions: string[] = [
    '/login',
    '/register',
    '/error404',
  ]

  static appHeaderExceptions: string[] = [
    '/login',
    '/register',
    '/error404',
  ]

  static appSideBarExceptions: string[] = [
    '/login',
    '/register',
    '/error404',
  ]

  static appFooterExceptions: string[] = [
    '/login',
    '/register',
    '/error404',
  ]
}
