import {Component, Input, OnInit} from '@angular/core';
import {Course} from "../../models/course";
import {CourseService} from "../../services/course.service";
import {CoursesPendingComponent} from "../courses-pending/courses-pending.component";
import {AbstractControl, FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged, map, Observable, Subject, switchMap} from "rxjs";
import {UserService} from "../../user/user.service";
import {UserInfo} from "../../models/user-info";

@Component({
  selector: 'course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})

export class CourseDetailsComponent implements OnInit {

  @Input() course!: Course;
  @Input() isAdmin!: boolean;
  @Input() isPending!: boolean;

  private partial = new Subject<string>();

  sport = '';

  formInputPendingCourse = new FormControl('',this.validatePlayer);
  candidates = new Array()

  constructor(private courseService: CourseService, private listPending: CoursesPendingComponent, private userService: UserService) {
  }

  ngOnInit(): void {
    this.sport=(this.course.sporttype == 2 ? 'TENNIS' : 'PADEL');

    const findPlayers = this.userService.findPlayers.bind(this.userService);

    this.partial.pipe(

      debounceTime(500),

      distinctUntilChanged(),

      switchMap(p => findPlayers(p, 5, this.course.players.concat(this.course.instructor)))

    ).subscribe(
      candidates => {
        this.candidates =   candidates
      }
    );

  }

  displayFn(user: UserInfo): string {
    return user && user.displayName ? user.displayName : '';
  }

  onInput(event: Event) {
    if (event.target instanceof HTMLInputElement)
      this.partial.next((event.target as HTMLInputElement).value);
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
    console.log(this.formInputPendingCourse.value)
  }

}
