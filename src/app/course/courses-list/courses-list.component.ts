import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";

@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.css']
})
export class CoursesListComponent implements OnInit {

  courses!: Observable<Course[]>;

  constructor(private courseService: CourseService) { }

  ngOnInit(): void {
    this.reloadData();
  }

  deleteCourses() {
    this.courseService.deleteAllCourses()
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    this.courses = this.courseService.getCoursesList();
  }

}
