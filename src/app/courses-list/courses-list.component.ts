import {Component, Input, OnInit} from '@angular/core';
import {CourseService} from "../services/course.service";
import {Observable} from "rxjs";
import {Course} from "../models/course";

@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.css']
})
export class CoursesListComponent implements OnInit {

  courses!: Observable<Course[]>
  @Input() isTennis!: boolean;

  constructor(private courseService: CourseService) { }

  ngOnInit(): void {
    if (this.isTennis)
      this.courses = this.courseService.getCoursesBySportAndYear(2, 2022, true)
    else
      this.courses = this.courseService.getCoursesBySportAndYear(4, 2022, true)
  }

}
