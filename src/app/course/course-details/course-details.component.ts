import {Component, Input, OnInit} from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {CoursesListComponent} from "../courses-list/courses-list.component";

@Component({
  selector: 'course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {

  @Input() course!: Course;

  constructor(private courseService: CourseService, private listComponent: CoursesListComponent) { }

  ngOnInit(): void {
  }

  deleteCourse() {
    this.courseService.deleteCourse(this.course.id)
      .subscribe(
        data => {
          console.log(data);
          this.listComponent.reloadData();
        },
        error => console.log(error));
  }

}
