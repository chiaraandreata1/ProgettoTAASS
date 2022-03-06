import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {UserService} from "../../services/user.service";
import {map, Observable, startWith, Subscription} from "rxjs";


import {FormControl} from "@angular/forms";
import {Court} from "../../models/court";
import {User} from "../../models/user";

@Component({
  selector: 'create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrls: ['./create-reservation.component.css'],
})
export class CreateReservationComponent implements OnInit {
  reservationsNOTAvailable!: Observable<Reservation[]>;
  courts!: Observable<Court[]>
  reservation: Reservation = new Reservation();
  court: Court = new Court();
  sportReservation = new FormControl();
  dateReservation = new FormControl();
  player1 = new FormControl();
  player2 = new FormControl();
  player3 = new FormControl();
  player4 = new FormControl();
  hoursAvailable = [9,10,11,12,13,14,15,16,17,18,19,20,21];
  submitted = false;
  arrNOTAvailableForHours = new Array();
  users = new Array();
  nameFormUsers = ['player1', 'player2', 'player3', 'player4'];
  filteredOptions!: Observable<User[]>;
  subscription = new Subscription();
  searchready = false;

  minDate: Date;
  maxDate: Date;

  constructor(private reservationService: ReservationService, private  userService: UserService) {
  this.minDate = new Date();
  this.maxDate = new Date();
  this.maxDate.setMonth(this.minDate.getMonth()+1)
}

  ngOnInit(): void {
    this.courts = this.reservationService.getCourtsList();
    var obsUsers = this.userService.getUsersList();
    obsUsers.toPromise()
      .then(
        data => {
          this.users = <Array<User>>data;
        }
      );
    this.filteredOptions = this.player1.valueChanges.pipe(
      map(value => (typeof value === 'string' ? value : value.username)),
      map(username => (username ? this._filter(username) : this.users.slice())),
    );
  }

  displayFn(user: User): string {
    return user && user.username ? user.username : '';
  }

  private _filter(username: string): User[] {
    const filterValue = username.toLowerCase();
    return this.users.filter(users => users.username.toLowerCase().includes(filterValue));
  }


  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  newReservation(): void{
    this.submitted = false;
    this.reservation = new Reservation();
  }

  save() {
    this.reservationService.createReservation(this.reservation)
      .subscribe(data => console.log(data), error => console.log(error));
    this.reservation = new Reservation();
    this.court = new Court();
  }

  createReservation(sportReservation: string, dateReservation: string, hour: number, courtId: number, courtType: string)
  {
    this.reservation.sportReservation = sportReservation;
    this.reservation.dateReservation = new Date(this.reservation.dateReservation.toString()).toISOString().split('T')[0];
    this.reservation.hourReservation = hour;
    this.court.id = courtId;
    this.court.type = courtType;
    this.reservation.courtReservation = this.court;
    this.submitted = true;
    this.save();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  reloadData() {
    if (this.dateReservation.value!=undefined && this.sportReservation.value!=undefined)
    {
      this.searchready = true;
      console.log(this.dateReservation.value.toISOString().split('T')[0])
      this.reservationsNOTAvailable = this.reservationService.getReservationByDateAndSport(new Date(this.dateReservation.value).toISOString().split('T')[0], this.sportReservation.value)
      this.reservationsNOTAvailable.toPromise()
      .then(
        data => {
          var reservationsNOTAvailableNOTobs = <Array<Reservation>>data;
          this.arrNOTAvailableForHours = new Array();
          for (var i=0; i<this.hoursAvailable.length; i++)
          {
            var arr = new Array();
            for (var j = 0; j<reservationsNOTAvailableNOTobs.length; j++)
            {
              if (reservationsNOTAvailableNOTobs[j].hourReservation == this.hoursAvailable[i])
                arr.push(reservationsNOTAvailableNOTobs[j].courtReservation.id);
            }
            this.arrNOTAvailableForHours.push(arr);
          }
        }
      );
    }
    else
      this.searchready = false;
  }

  deleteReservation(id: number) {
    this.reservationService.deleteReservation(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  show(){
    console.log(this.users)
  }
}


