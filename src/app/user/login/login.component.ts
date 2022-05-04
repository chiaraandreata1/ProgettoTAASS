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

  private errorMessage? : string = undefined;

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
      this.userService.getCurrentUser().subscribe(
        user => {
          this.router.navigate([])
        },
        error => {
          this.errorMessage = error.error.message;
        }
      )
    }
  }

}
