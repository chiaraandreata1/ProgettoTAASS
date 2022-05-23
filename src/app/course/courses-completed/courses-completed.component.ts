import {Component, Injectable, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-courses-completed',
  templateUrl: './courses-completed.component.html',
  styleUrls: ['./courses-completed.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class CoursesCompletedComponent implements OnInit {

  //isAdmin = false;

  completeCourses!: Observable<Course[]>;
  rangeYears!: any;
  yearCourse!: number;
  sportCourse!: string;

  levels = ["Beginner", "Intermediate", "Pro"];

  constructor(private courseService: CourseService, private userService: UserService) {
    let year = new Date().getFullYear();
    this.rangeYears = new Array();
    for (let i=0; i<5; i++)
      this.rangeYears.push(year-i);
  }

  ngOnInit(): void {
    //this.isAdmin = this.userService.getRoleUserLogged() == "admin";
  }

  findCompleteCoursesBySportAndYear() {
    if (this.sportCourse!='' && this.yearCourse)
      this.completeCourses = this.courseService.getCoursesBySportAndYear(this.sportCourse,this.yearCourse, false);
  }

  debugButton() {
    console.log(this.completeCourses);
  }
}
