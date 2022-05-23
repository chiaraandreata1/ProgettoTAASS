import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../services/token-storage.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../user.service"

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginLink = LoginComponent.loginLink;

  errorMessage? : string;
  error?: any;

  constructor(
    private tokens: TokenStorageService,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get("token");
    const error = this.route.snapshot.queryParamMap.get("error");

    if (token) {
      this.tokens.saveToken(token);
      this.userService.me().subscribe(
        user => {
          this.tokens.saveUser(user);
          console.log("navigate");
          this.router.navigate([""]);
        },
        error => {
          this.errorMessage = error.error.message;
        }
      )
    } else if (this.tokens.getToken()) {
      this.userService.me().subscribe(console.log);
      console.log("navigate");
      this.router.navigate([""]);
    } else if (error) {
      this.errorMessage = error;
    }
  }

  public static loginLink(provider: string): string {
    return `http://localhost:8080/api/v1/user/oauth2/authorization/${provider}?redirect_uri=http://localhost:8080/login`
  }

  logout() {
    this.tokens.signOut();
  }
}
