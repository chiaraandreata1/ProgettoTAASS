import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {TokenStorageService} from "../services/token-storage.service";
import {Router} from "@angular/router";
import {Observable, tap} from "rxjs";
import {UserService} from "../user/user.service";

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private token: TokenStorageService, private router: Router, private userService: UserService) {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log("something intercepted");
    let authReq = req;
    const loginPath = '/login';
    const token = this.token.getToken();
    if (token != null) {
      authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
    }

    return next.handle(authReq).pipe(
      tap({
        error: err => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 403) {
              this.token.signOut();
              this.router.navigate(["login"], {queryParams: {message: "Not authorized"}});
            }
          }
        }
      })
    )

    // return next.handle(authReq).pipe( tap(() => {},
    //   (err: any) => {
    //     if (err instanceof HttpErrorResponse) {
    //       if (err.status !== 401 || window.location.pathname === loginPath) {
    //         return;
    //       }
    //       this.token.signOut();
    //       window.location.href = loginPath;
    //     }
    //   }
    // ));
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
