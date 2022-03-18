import { Component, OnInit } from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {

  course: Course = new Course();

  day1 = new FormControl();
  day2 = new FormControl();
  dateEndRegistration = new FormControl();

  weekday = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
  submitted = false;

  minDate: Date;

  constructor(private courseService: CourseService) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 14);
    this.course.dayslesson = new Array();
  }

  ngOnInit(): void {
  }

  setDate() {
    this.course.endDateRegistration = new Date(this.dateEndRegistration.value.toString()).toISOString().split('T')[0];
  }

  setDay(i: number) {
    this.course.dayslesson[i] = i==0 ? this.day1.value : this.day2.value;
  }

  setNumberWeeks() {
    switch (this.course.priceCourse.toString()) //non so perchè lo switch con gli interi non funziona
    {
      case '240':
        this.course.numberweeks = 12;
        break;
      case '400':
        this.course.numberweeks = 24;
    }
  }

  save(){
    this.course.players = new Array();
    this.courseService.createCourse(this.course)
      .subscribe(data => console.log(data), error => console.log(error));
    this.course = new Course();
  }

  onSubmit() {
    this.submitted = true;
    console.log(this.course);
    this.save();
  }

  newCourse(){
    this.course = new Course();
    this.day1.reset();
    this.day2.reset();
    this.course.dayslesson = new Array();
    this.submitted = false;
  }

}
