import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";
import {Router} from "@angular/router";
import { LoginComponent } from './user/login/login.component';
import {UserService} from "./user/user.service";
import {Observable, tap} from "rxjs";
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

  public isManager$: Observable<boolean>;

  constructor(
    private tokens: TokenStorageService,
    public users: UserService,
    private router: Router
  ) {
    users.currentUserObserver().subscribe(console.log);
    this.isManager$ = users.isManager().pipe(tap(console.log));
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

}
