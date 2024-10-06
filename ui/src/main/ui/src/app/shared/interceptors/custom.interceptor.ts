import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, of, tap} from 'rxjs';
import {NotificationsService} from "angular2-notifications";

@Injectable()
export class CustomInterceptor implements HttpInterceptor {

  constructor(private notifications: NotificationsService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
    const authReq = request.clone({
      setHeaders: {
        Authorization: `Bearer your-auth-token`, // Replace with your actual token logic
        'ngrok-skip-browser-warning': 'true'
      }
    });

    return next.handle(authReq).pipe(
      tap((event: HttpEvent<any>) => {
      }), catchError((response: any) => {
        if (response instanceof HttpErrorResponse) {
          this.notifications.error(
            "Something went wrong",
            `We logged this issue, we will fix it as soon as possible!`
            , {
              theClass: 'error',
              timeOut: 3000
            }
          );
        }
        return of(null);
      })
    );

    // return next.handle(authReq).pipe(
    //   tap(
    //     (event: HttpEvent<any>) => {
    //       if (event instanceof HttpResponse && event.status !== 200) {
    //         console.log('success')
    //       }
    //     },
    //     (error: HttpErrorResponse) => {
    //       this.notifications.error(
    //         "Something went wrong",
    //         `We logged this issue, we will fix it as soon as possible!`
    //         , {
    //           theClass: 'error',
    //           timeOut: 2000
    //         }
    //       );
    //     }
    //   )
    // );
  }

}
