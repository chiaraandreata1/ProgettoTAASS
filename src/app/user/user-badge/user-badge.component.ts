import {Component, Input, OnInit} from '@angular/core';
import {UserInfo} from "../../models/user-info";
import {Observable} from "rxjs";
import {UserService} from "../user.service";

@Component({
  selector: 'app-user-badge',
  templateUrl: './user-badge.component.html',
  styleUrls: ['./user-badge.component.css']
})
export class UserBadgeComponent implements OnInit {

  @Input() userID?: number;
  @Input() user?: UserInfo;
  @Input() me?: boolean;
  error?: string;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    if (!this.user) {
      if (this.userID) {
        this.userService.getUser(this.userID)
          .subscribe(value => {
            this.user = value;
          })
      } else if (this.me) {
        this.userService.me()
          .subscribe(value => {
            this.user = value;
          })
      } else {
        this.error = "Who should I show?"
      }
    }
  }

}
