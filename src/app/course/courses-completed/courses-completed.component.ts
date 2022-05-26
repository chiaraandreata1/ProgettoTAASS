import {Component, Injectable, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {OldUserService} from "../../services/user.service";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'app-courses-completed',
  templateUrl: './courses-completed.component.html',
  styleUrls: ['./courses-completed.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class CoursesCompletedComponent implements OnInit {

  isAdmin = false;

  completeCourses!: Observable<Course[]>;
  rangeYears!: any;
  yearCourse!: number;
  sportCourse!: string;

  levels = ["Beginner", "Intermediate", "Pro"];

  subscription = new Subscription()

  constructor(private courseService: CourseService, private userService: OldUserService, private NewUserService: UserService) {
    let year = new Date().getFullYear();
    this.rangeYears = new Array();
    for (let i=0; i<5; i++)
      this.rangeYears.push(year-i);
  }

  ngOnInit(): void {
    this.subscription = this.NewUserService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  findCompleteCoursesBySportAndYear() {
    if (this.sportCourse!='' && this.yearCourse)
      this.completeCourses = this.courseService.getCoursesBySportAndYear(this.sportCourse,this.yearCourse, false);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  debugButton() {
    console.log(this.completeCourses);
  }
}
