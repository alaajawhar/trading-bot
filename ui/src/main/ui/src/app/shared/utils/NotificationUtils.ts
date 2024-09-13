import {Injectable} from "@angular/core";
import {NotificationsService} from "angular2-notifications";

@Injectable({
  providedIn: 'root',
})
export class NotificationUtils {
  constructor(private notifications: NotificationsService) {
  }

  showSuccessMessage(title: string, subtitle: string) {
    this.notifications.success(
      title,
      subtitle
      , {
        theClass: 'success',
        timeOut: 2000,
        showProgressBar: false
      }
    );
  }

  showWarningMessage(title: string, subtitle: string) {
    this.notifications.warn(
      title,
      subtitle
      , {
        theClass: 'success',
        timeOut: 2000,
        showProgressBar: false
      }
    );
  }

  showErrorMessage(title: string, subtitle: string) {
    this.notifications.error(
      title,
      subtitle
      , {
        theClass: 'success',
        timeOut: 2000,
        showProgressBar: false
      }
    );
  }
}
