import {Component, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from '@angular/common'
import {HeaderNotification, HeaderProfile} from "./header.component.model";
import {Router} from "@angular/router";
import {AppConstants} from "../../app.constants";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  /* SEARCH BAR */
  isShowSearchBar: boolean = false;

  /* NOTIFICATION */
  isShowNotification: boolean = true;
  notificationList: HeaderNotification = {
    message: "You have 4 new notifications",
    items: [
      {
        id: 'id1',
        bootstrapIcon: 'text-warning',
        title: 'Lorem Ipsum',
        subtitle1: 'Quae dolorem earum veritatis oditseno',
        subtitle2: '30 min. ago',
      },
      {
        id: 'id2',
        bootstrapIcon: 'text-danger',
        title: 'Atque rerum nesciunt',
        subtitle1: 'Quae dolorem earum veritatis oditseno',
        subtitle2: '1 hr. ago',

      },
      {
        id: 'id3',
        bootstrapIcon: 'text-success',
        title: 'Sit rerum fuga',
        subtitle1: 'Quae dolorem earum veritatis oditseno',
        subtitle2: '2 hrs. ago',
      },
      {
        id: 'id4',
        bootstrapIcon: 'text-primary',
        title: 'Dicta reprehenderit',
        subtitle1: 'Quae dolorem earum veritatis oditseno',
        subtitle2: '4 hrs. ago',
      },
    ]
  }

  onNotificationItemClick(notificationId: string) {
    console.log(`On a specific notification click. notificationId : [${notificationId}]`)
  }

  onShowAllNotificationClick() {
    console.log("On show all notification link button.")
  }

  /* PROFILE */
  isShowProfile: boolean = true;
  profileList: HeaderProfile = {
    fullName: "Alaa Jawhar",
    role: "Software Developer",
    items: [
      {
        id: 'profileId2',
        bootstrapIcon: 'bi bi-gear',
        onClick: () => this.router.navigate(['/profile']),
        title: 'Account Settings',
      },
      {
        id: 'profileId3',
        bootstrapIcon: 'bi bi-box-arrow-right',
        onClick: () => this.router.navigate(['/login']),
        title: 'Sign Out',
      },
    ],
  };

  constructor(@Inject(DOCUMENT) private document: Document, private router: Router) {
  }

  ngOnInit(): void {
  }

  applicationName: string = AppConstants.APPLICATION_NAME;

  sidebarToggle() {
    this.document.body.classList.toggle('toggle-sidebar');
  }
}
