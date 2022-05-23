import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {Router} from "@angular/router";
import { LoginComponent } from './user/login/login.component';
import {UserService} from "./user/user.service";
import {tap} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {UserInfo} from "./models/user-info";


declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'NetBall';
  description = 'Frontend implementation for tennis and padel facility system';

  error?: string;

  loginLink = LoginComponent.loginLink;

  user?: UserInfo;
  me: any;
  a: any;

  constructor(
    private tokens: TokenStorageService,
    private users: UserService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.tokens.getCurrentUserSubject().subscribe(
      user => {
        console.log(user);
        this.user = user;
        console.log(this.user);
      }
    )
  }


  logout() {
    this.users.logout().subscribe(() => {
      this.tokens.signOut();
      this.router.navigate([]);
    })
  }

  checkProfile() {
    this.users.getCurrentUser(true).subscribe(console.log);
    this.users.test().pipe().subscribe(console.log);
  }

  getMe() {
    this.router.navigate(["me"])
  }

  test() {
    console.log("test");

    this.users.test().subscribe(
      {
        next: console.log,
        error: err => {
          if (err instanceof HttpErrorResponse) {
            switch (err.status) {
              case 401:
                this.error = "Not authenticated";
                break;
              case 403:
                this.error = "Not authorized";
                break;
            }
          } else {
            console.log(typeof err)
          }
        }
      }
    )
  }

  getA() {
    this.users.a().subscribe(
      {
        next: msg => {
          this.a = msg
          console.log(msg)
        },
        error: err => {
          this.error = err
          console.log(err)
        }
      }
    )
  }
}
