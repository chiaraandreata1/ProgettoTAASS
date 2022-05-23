import {Component, Input, OnInit} from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {CoursesPendingComponent} from "../courses-pending/courses-pending.component";
import {AbstractControl, FormControl} from "@angular/forms";
import {User} from "../../models/user";
import {map, Observable} from "rxjs";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})

export class CourseDetailsComponent implements OnInit {

  @Input() course!: Course;
  //@Input() isAdmin!: boolean;
  @Input() isPending!: boolean;

  user1!: User;

  formInputPendingCourse = new FormControl('',this.validatePlayer);
  users = new Array()
  filterOptions = new Observable<User[]>();

  constructor(private courseService: CourseService, private listPending: CoursesPendingComponent, private userService: UserService) {

  }

  ngOnInit(): void {
    let obsUsers = this.userService.getUsersList();
    obsUsers.toPromise()
      .then(
        data => {
          this.users = <Array<User>>data;
          let userSelectable = this.users.filter(value => !this.course.players.includes(value.id) && value.id!=this.course.instructor);
          this.filterOptions = this.formInputPendingCourse.valueChanges.pipe(
            map(user => (typeof user === 'string' ? user : user.username)),
            map(user => (user ? this._filter(user, userSelectable) : userSelectable.slice())),
          );
        }
      );
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
          let userSelectable = this.users.filter(value => !this.course.players.includes(value.username) && value.username!=this.course.instructor);
          this.filterOptions = this.formInputPendingCourse.valueChanges.pipe(
            map(user => (typeof user === 'string' ? user : user.username)),
            map(user => (user ? this._filter(user, userSelectable) : userSelectable.slice())),
          );
        }
      );
  }

  validatePlayer(control: AbstractControl): {[key: string]: any} | null  {
    if (typeof control.value == 'string') {
      return { 'userInvalid': true };
    }
    return null;
  }

  //dopo che aggiungi un user nell'input label, questo dopo aver premuto il pulsante add aggiornerà la lista players di quel corso con una PUT
  //nel caso in cui aggiungi un giocatore che completerà il corso, bisognerà creare tutte le reservation
  addNewPlayer(){
    this.course.players.push(this.formInputPendingCourse.value.id);
    let body = Course.toJSON(this.course);

    this.courseService.updateCourse(this.course.id, body)
      .subscribe(data => {
        console.log(data);
        this.listPending.reloadData();
      }, error => console.log(error));
    console.log(this.course.players.length);
  }

  deleteCourse() {
    this.courseService.deleteCourse(this.course.id)
      .subscribe(
        data => {
          console.log(data);
          this.listPending.reloadData();
        },
        error => console.log(error));
  }

  debugButton(){
    console.log(this.users)
  }

}
