import {Component, Injectable, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {ReservationService} from "../../services/reservation.service";
import {User} from "../../models/user";
import {DummyCourt} from "../../models/dummyCourt";
import {Reservation} from "../../models/reservation";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'app-courses-pending',
  templateUrl: './courses-pending.component.html',
  styleUrls: ['./courses-pending.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class CoursesPendingComponent implements OnInit {

  isAdmin = false;
  pendingCourses!: Observable<Course[]>;
  pendingCoursesObj = new Array();
  sportCourse = new FormControl();

  formsInputPendingCourses = new FormGroup({});

  users = new Array();
  filtersOptions = new Array();

  weekday = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  levels = ["Beginner", "Intermediate", "Pro"];

  subscription = new Subscription()


  constructor(private courseService: CourseService, private reservationService: ReservationService, private NewUserService: UserService) { }

  ngOnInit(): void {
    this.subscription = this.NewUserService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  reloadData() {
    //this.course = new Course();
    if (this.sportCourse.value){
      console.log(this.sportCourse.value)
      this.pendingCourses = this.courseService.getCoursesBySportAndYear(this.sportCourse.value,new Date().getFullYear(), true);
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  debugButton(){
    let date = new Date('2022-03-30');
    console.log(date);
  }

}
