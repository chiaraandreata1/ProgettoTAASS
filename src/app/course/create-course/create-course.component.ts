import { Component, OnInit } from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {AbstractControl, FormControl, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {Serialization} from "../../utilities/serialization";
import {UserService} from "../../user/user.service";
import {UserInfo} from "../../models/user-info";
import {Court} from "../../models/court";
import {FacilityService} from "../../services/facility.service";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {

  instructors = new Array();
  levels = ['Beginner', 'Intermediate', 'Pro'];

  //info del corso-----------
  sporttype=0;
  instructorCourse = new FormControl('', [this.validateInstructor]);
  daycourse= '';
  levelcourse = '';
  hourLesson = new FormControl('', [Validators.min(9), Validators.max(21)])
  weeksLesson = new FormControl('', [Validators.min(0), Validators.max(52)])
  priceCourse=NaN;
  courtCourse=NaN;
  dateEndRegistration = new FormControl();
  //-------------------------

  filter: Observable<UserInfo[]>;

  weekday = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  submitted = false;

  minDateEndRegistration: Date;

  today = new Date(); //utilizzato solo nel check che le date scelte non siano quelle di oggi

  userID = 0;
  tennisCourts = new Array(); padelCourts = new Array();

  constructor(private courseService: CourseService, private userService: UserService, private facilityService: FacilityService) {
    this.minDateEndRegistration = new Date();
    this.minDateEndRegistration.setDate(this.minDateEndRegistration.getDate() + 14);
    //costruisco l'array degli istruttori
    this.userService.findInstructors().toPromise()
      .then(
        data => {
          this.instructors = <Array<UserInfo>>data;
        }
      );
    this.filter = this.instructorCourse.valueChanges.pipe(
      map(value => (typeof value === 'string' ? value : value.displayName)),
      map(name => (name ? this._filter(name) : this.instructors.slice())),
    );
  }

  ngOnInit(): void {
    let id:number = this.userService.getCurrentUser()?.id || 0;
    this.userID=id;

    this.facilityService.getCourts().toPromise().then(data => {
      let allCourts = <Array<Court>>data;
      for (let i = 0; i<allCourts.length; i++)
        if (allCourts[i].sport.name=="Tennis") this.tennisCourts.push(allCourts[i].id);
        else this.padelCourts.push(allCourts[i].id)
    })
  }

  //blocco funzioni per la barra suggerimenti-----------------------------------------------------------------------------------
  //queste ultime 3 funzioni forse si potrebbero generalizzare. Sono simili a quelle della create reservation
  displayFn(user: UserInfo): string {
    return user && user.displayName ? user.displayName : '';
  }

  private _filter(displayName: string): UserInfo[] {
    const filterValue = displayName.toLowerCase();
    return this.instructors.filter(users => users.displayName.toLowerCase().includes(filterValue));
  }

  validateInstructor(control: AbstractControl): {[key: string]: any} | null  {
    if (typeof control.value == 'string') {
      return { 'userInvalid': true };
    }
    return null;
  }
  //----------------------------------------------------------------------------------------------------------------------------

  //Per la gestione dinamica della label Instructor------------------------------------------------------
  enableInstructorInput(){
    this.instructorCourse.setValue('');
    this.instructorCourse.enable();
  }

  disableInstructorInput(){
    this.instructorCourse.disable();
  }
  //-----------------------------------------------------------------------------------------------------

  save(){
    let stringDateEndRegistration = Serialization.serializeDateTime(this.dateEndRegistration.value);
    let dateFirstLesson = new Date(this.dateEndRegistration.value);
    dateFirstLesson.setDate(dateFirstLesson.getDate() + 7 - dateFirstLesson.getDay() + (this.weekday.indexOf(this.daycourse) + 1))
    dateFirstLesson.setHours(this.hourLesson.value)
    let stringDateFirstLesson = Serialization.serializeDateTime(dateFirstLesson) //todo commentare quando torna il serializzatore
    let course = new Course(-1, this.userID, this.sporttype, this.instructorCourse.value.id, this.levelcourse, [], this.daycourse.toLowerCase(), this.hourLesson.value, this.weeksLesson.value, this.priceCourse, this.courtCourse, stringDateEndRegistration, stringDateFirstLesson, []);
    console.log(course);
    let body = Course.toJSON(course);
    this.courseService.createCourse(body)
      .subscribe(data => console.log(data), error => console.log(error));
    //this.course = new Course();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  newCourse(){
    this.sporttype=0;
    this.instructorCourse.enable()
    this.instructorCourse.reset()
    this.daycourse= '';
    this.hourLesson.reset()
    this.weeksLesson.reset()
    this.priceCourse=NaN;
    this.courtCourse=NaN;
    this.dateEndRegistration.reset()

    this.submitted = false;
  }

  debugButton(){
    console.log(this.instructors[0])
  }

}
