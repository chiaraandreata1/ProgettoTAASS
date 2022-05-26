import { Component, OnInit } from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {AppComponent} from "../../app.component";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'app-course-main',
  templateUrl: './course-main.component.html',
  styleUrls: ['./course-main.component.css']
})
export class CourseMainComponent implements OnInit {

  isAdmin = false;
  subscription = new Subscription();


  constructor(private NewUserService: UserService, private app: AppComponent) {
  }

  ngOnInit(): void {
    this.subscription = this.NewUserService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
