import {Component, Injectable, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {UserService} from "../../services/user.service";
import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {ReservationService} from "../../services/reservation.service";
import {User} from "../../models/user";
import {DummyCourt} from "../../models/dummyCourt";
import {Reservation} from "../../models/reservation";

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

  constructor(private courseService: CourseService, private userService: UserService, private reservationService: ReservationService) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.getRoleUserLogged() == "admin";
  }

  reloadData() {
    //this.course = new Course();
    if (this.sportCourse.value){
      console.log(this.sportCourse.value)
      this.pendingCourses = this.courseService.getCoursesBySportAndYear(this.sportCourse.value,new Date().getFullYear(), true);
    }
  }

  debugButton(){
    let date = new Date('2022-03-30');
    console.log(date);
  }

}
