import { Component, OnInit } from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {AbstractControl, FormControl, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";

interface Courts {
  value: number;
  viewValue: string;
}

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {

  tennisCourts: Courts[] = [
    {viewValue: 'Court 1', value: 1},
    {viewValue: 'Court 2', value: 2},
    {viewValue: 'Court 3', value: 3},
  ]

  padelCourts: Courts[] = [
    {viewValue: 'Court 4', value: 4},
    {viewValue: 'Court 5', value: 5},
    {viewValue: 'Court 6', value: 6},
  ]

  instructors = new Array();
  levels = ['Beginner', 'Intermediate', 'Pro'];

  //info del corso-----------
  sporttype='';
  instructorCourse = new FormControl('', [this.validateInstructor]);
  daycourse= '';
  levelcourse = '';
  hourLesson = new FormControl('', [Validators.min(9), Validators.max(21)])
  weeksLesson = new FormControl('', [Validators.min(0), Validators.max(52)])
  priceCourse=NaN;
  courtCourse=NaN;
  dateEndRegistration = new FormControl();
  //-------------------------

  filter: Observable<User[]>;

  weekday = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  submitted = false;

  minDateEndRegistration: Date;
  isAdmin: Boolean;

  today = new Date(); //utilizzato solo nel check che le date scelte non siano quelle di oggi

  constructor(private courseService: CourseService, private userService: UserService) {
    this.isAdmin = this.userService.getRoleUserLogged()=='admin';
    this.minDateEndRegistration = new Date();
    this.minDateEndRegistration.setDate(this.minDateEndRegistration.getDate() + 14);
    //costruisco l'array degli istruttori
    this.userService.getUsersByType('instructor').toPromise()
      .then(
        data => {
          this.instructors = <Array<User>>data;
        }
      );
    this.filter = this.instructorCourse.valueChanges.pipe(
      map(value => (typeof value === 'string' ? value : value.username)),
      map(name => (name ? this._filter(name) : this.instructors.slice())),
    );
  }

  ngOnInit(): void {
  }

  //blocco funzioni per la barra suggerimenti-----------------------------------------------------------------------------------
  //queste ultime 3 funzioni forse si potrebbero generalizzare. Sono simili a quelle della create reservation
  displayFn(user: User): string {
    return user && user.username ? user.username : '';
  }

  private _filter(username: string): User[] {
    const filterValue = username.toLowerCase();
    return this.instructors.filter(users => users.username.toLowerCase().includes(filterValue));
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
    let dateFirstLesson = new Date(this.dateEndRegistration.value);
    dateFirstLesson.setDate(dateFirstLesson.getDate() + 7 - dateFirstLesson.getDay() + (this.weekday.indexOf(this.daycourse) + 1))
    dateFirstLesson.setHours(this.hourLesson.value)
    console.log(dateFirstLesson)
    let course = new Course(-1, this.sporttype, this.instructorCourse.value.id, this.levelcourse, [], this.daycourse.toLowerCase(), this.hourLesson.value, this.weeksLesson.value, this.priceCourse, this.courtCourse, this.dateEndRegistration.value, dateFirstLesson);
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
    this.sporttype='';
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
