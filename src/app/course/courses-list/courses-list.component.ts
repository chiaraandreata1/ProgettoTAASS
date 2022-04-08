import { Component, OnInit } from '@angular/core';
import {map, Observable} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {ReservationService} from "../../services/reservation.service";
import {Reservation} from "../../models/reservation";
import {DummyCourt} from "../../models/dummyCourt";

@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.css']
})
export class CoursesListComponent implements OnInit {
  isAdmin = false;

  courses!: Observable<Course[]>;
  obsPendingCourses!: Observable<Course[]>;
  course: Course = new Course();
  pendingCourses = new Array();

  formsInputPendingCourses = new FormGroup({});

  users = new Array();
  filtersOptions = new Array();

  weekday = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

  constructor(private courseService: CourseService, private userService: UserService, private reservationService: ReservationService) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.getRoleUserLogged() == "admin";
    this.reloadData();
    this.prepareUserOptions();
    this.addOnFormGroup();
  }

  displayFn(user: User): string {
    return user && user.username ? user.username : '';
  }

  private _filter(username: string, usersSelectable: User[]): User[] {
    const filterValue = username.toLowerCase();
    return usersSelectable.filter(users => users.username.toLowerCase().includes(filterValue));
  }

  //in realtà questa funzione e la validate ci sono già in reservation. Bisogna renderla globale
  prepareUserOptions(): void {
    let obsUsers = this.userService.getUsersList();
    obsUsers.toPromise()
      .then(
        data => {
          this.users = <Array<User>>data;
        }
      );
  }

  validatePlayer(control: AbstractControl): {[key: string]: any} | null  {
    if (typeof control.value == 'string') {
      return { 'userInvalid': true };
    }
    return null;
  }

  addOnFormGroup(){
    this.courses.toPromise()
      .then(
        data => {
          let objCourses = <Array<Course>>data;
          for (let i = 0; i<objCourses.length; i++)
            if (objCourses[i].players.length!=3)
            {
              this.formsInputPendingCourses.addControl(objCourses[i].id.toString(), new FormControl('', [this.validatePlayer]));
              this.pendingCourses.push(objCourses[i]);
            }
          for (let i = 0; i<this.pendingCourses.length; i++)
          {
            let userSelectable = this.users.filter(value => !this.pendingCourses[i].players.includes(value.username) && value.username!=this.pendingCourses[i].instructor);
            let filteredOptions = this.formsInputPendingCourses.controls[this.pendingCourses[i].id].valueChanges.pipe(
              map(user => (typeof user === 'string' ? user : user.username)),
              map(user => (user ? this._filter(user, userSelectable) : userSelectable.slice())),
            );
            this.filtersOptions.push(filteredOptions);
          }
        }
      );
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
    this.course = new Course();
    this.courses = this.courseService.getCoursesList()
    this.courses.toPromise().then( data => { //TODO: perfezionare. Non sempre si vede il course con i players aggiornato
      this.obsPendingCourses = this.courses.pipe(map(courses => courses.filter(course => course.players.length < 3)));
    });
  }

  addNewPlayer(courseId: number){
    let course = this.pendingCourses.find(x => x.id === courseId);
    course.players.push(this.formsInputPendingCourses.controls[courseId.toString()].value.username)
    this.courseService.updateCourse(courseId, course)
      .subscribe(data => {
        console.log(data);
        let course = <Course>data;
        let index = 0;
        for (; index<this.pendingCourses.length; index++)
          if (this.pendingCourses[index].id == course.id)
            break;
        let userSelectable = this.users.filter(value => !course.players.includes(value.username) && value.username!=course.instructor);
        this.formsInputPendingCourses.controls[this.pendingCourses[index].id].reset();
        this.filtersOptions[index] = this.formsInputPendingCourses.controls[this.pendingCourses[index].id].valueChanges.pipe(
          map(user => (typeof user === 'string' ? user : user.username)),
          map(user => (user ? this._filter(user, userSelectable) : userSelectable.slice())),
        );
      }, error => console.log(error));
    //TODO: questa operazione dovrà essere fatta dal server
    if (course.players.length==3)
    {
      let date = new Date(course.endDateRegistration);
      date.setMonth(date.getMonth()+1);

      let reservationCourse = new Reservation();
      reservationCourse.sportReservation = course.sporttype;
      reservationCourse.players = course.players;
      reservationCourse.players.push(course.instructor);
      date.setDate(date.getDate() + 7 - date.getDay() + this.weekday.indexOf(course.daycourse));
      reservationCourse.hourReservation = course.hourlesson;
      reservationCourse.typeReservation = "lesson";
      let courtCourse = new DummyCourt();
      courtCourse.id = course.courtCourse;
      courtCourse.type = course.sporttype.toLowerCase();
      reservationCourse.courtReservation = courtCourse;
      for (let i = 0; i<course.numberweeks; i++)
      {
        reservationCourse.dateReservation = date.toISOString().split('T')[0];
        this.reservationService.createReservation(reservationCourse)
          .subscribe(data => console.log(data), error => console.log(error));
        date.setDate(date.getDate() + 7);
      }
    }
    //-----------------------------------------------------------------------------------------------------
    this.reloadData();
  }

  show(){
    let date = new Date('2022-03-30');
    console.log(date);
  }

}
