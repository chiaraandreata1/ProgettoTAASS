import { Component, OnInit } from '@angular/core';
import {UserInfo} from "../../models/user-info";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../user.service";

@Component({
  selector: 'app-show-user',
  templateUrl: './show-user.component.html',
  styleUrls: ['./show-user.component.css']
})
export class ShowUserComponent implements OnInit {

  userID?: number;
  error?: string;

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    let queryParamMap = this.route.snapshot.paramMap;
    let userID = queryParamMap.get("id");
    if (userID)
      this.userID = +userID;
    else
      this.error = "Missing user id";
  }

}
