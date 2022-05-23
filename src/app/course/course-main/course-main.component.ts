import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-course-main',
  templateUrl: './course-main.component.html',
  styleUrls: ['./course-main.component.css']
})
export class CourseMainComponent implements OnInit {

  //isAdmin = false;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    //this.isAdmin = this.userService.getRoleUserLogged() == "admin";
  }

}
