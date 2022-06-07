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
  isLogged=false;

  constructor(private NewUserService: UserService, private app: AppComponent) {
  }

  ngOnInit(): void {
    let id:number = this.NewUserService.getCurrentUser()?.id || 0;
    this.isLogged=id>0;
    this.subscription = this.NewUserService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
