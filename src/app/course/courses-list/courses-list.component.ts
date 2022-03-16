import { Component, OnInit } from '@angular/core';
import {map, Observable} from "rxjs";
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {FormControl, FormGroup} from "@angular/forms";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.css']
})
export class CoursesListComponent implements OnInit {

  courses!: Observable<Course[]>;
  course: Course = new Course();
  pendingCourses = new Array();

  formsInputPendingCourses = new FormGroup({});

  users = new Array();
  filtersOptions = new Array();

  constructor(private courseService: CourseService, private userService: UserService) { }

  ngOnInit(): void {
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

  //in realtà questa funzione c'è già in reservation. Bisogna renderla globale
  prepareUserOptions(): void {
    let obsUsers = this.userService.getUsersList();
    obsUsers.toPromise()
      .then(
        data => {
          this.users = <Array<User>>data;
        }
      );
  }

  addOnFormGroup(){
    this.courses.toPromise()
      .then(
        data => {
          let objCourses = <Array<Course>>data;
          for (let i = 0; i<objCourses.length; i++)
            if (objCourses[i].players.length!=3)
            {
              this.formsInputPendingCourses.addControl(objCourses[i].id.toString(), new FormControl());
              this.pendingCourses.push(objCourses[i]);
            }
          for (let i = 0; i<this.pendingCourses.length; i++)
          {
            let userSelectable = this.users.filter(value => !this.pendingCourses[i].players.includes(value.username));
            console.log(this.formsInputPendingCourses.controls[this.pendingCourses[i].id]);
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
    this.courses = this.courseService.getCoursesList();
  }

  addNewPlayer(courseId: number){
    let course = this.pendingCourses.find(x => x.id === courseId);
    course.players.push(this.formsInputPendingCourses.controls[courseId.toString()].value.username)
    this.courseService.updateCourse(courseId, course)
      .subscribe(data => console.log(data), error => console.log(error));
    window.location.reload();
  }

  show(){
    console.log(this.users);
  }

}
