import { Component, OnInit } from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {AbstractControl, FormControl, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit { //RIGHE 35 E 110 sono commentate perchè per ora abbiamo un unico giorno di lezione a settimana (da array a stringa)

  course: Course = new Course();

  instructors = new Array();

  instructorCourse = new FormControl('', [this.validateInstructor]);
  hourLesson = new FormControl('', [Validators.min(9), Validators.max(21)])
  filter: Observable<User[]>;

  day1 = new FormControl();
  day2 = new FormControl();
  dateEndRegistration = new FormControl();

  weekday = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  submitted = false;

  minDate: Date;
  isAdmin: Boolean;

  constructor(private courseService: CourseService, private userService: UserService) {
    this.isAdmin = this.userService.getRoleUserLogged()=='admin';
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 14);
    //this.course.dayslesson = new Array();
    //costruisco l'array degli istruttori
    this.userService.getUsersByType('instructor').toPromise()
      .then(
        data => {
          this.instructors = <Array<User>>data;
        }
      );
    this.filter = this.instructorCourse.valueChanges.pipe(
      map(user => (typeof user === 'string' ? user : user.username)),
      map(user => (user ? this._filter(user) : this.instructors.slice())),
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

  setDate() {
    this.course.endDateRegistration = new Date(this.dateEndRegistration.value.toString()).toISOString().split('T')[0];
  }

  /* nel caso volessimo più giorni è necessaria una onchange nel radio button con questa funzione, con i che è l'indice dell'array days
  setDay(i: number) {
    this.course.dayslesson[i] = i==0 ? this.day1.value : this.day2.value;
  }
  */

  enableInstructorInput(){
    this.instructorCourse.reset(); this.instructorCourse.enable(); this.course.instructor="";
  }

  disableInstructorInput(){
    this.instructorCourse.disable(); this.course.instructor = this.instructorCourse.value.username;
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
    this.instructorCourse.reset();
    //this.course.dayslesson = new Array();
    this.submitted = false;
  }

  debugButton(){
    console.log(this.course.numberweeks)
    console.log(this.course.priceCourse);
    console.log(this.course.daycourse);
    console.log(this.course.instructor);
    console.log(this.course.endDateRegistration);
    console.log(this.course.hourlesson);
    console.log(this.course.sporttype);
  }

}
