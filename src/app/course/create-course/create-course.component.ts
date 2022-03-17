import { Component, OnInit } from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {

  course: Course = new Course();

  submitted = false;

  constructor(private courseService: CourseService) { }

  ngOnInit(): void {
  }

  save(){
    //eventuali aggiunte
  this.courseService.createCourse(this.course)
  .subscribe(data => console.log(data), error => console.log(error));
    this.course = new Course();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

}
