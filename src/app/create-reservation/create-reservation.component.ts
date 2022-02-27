import { Component, OnInit } from '@angular/core';

import {Reservation} from "../reservation";
import {ReservationService} from "../reservation.service";
import {Observable} from "rxjs";

import * as $ from 'jquery';

/*export class DatepickerComponent implements OnInit {
  constructor() { }
  minDate = new Date(2022, 1, 1);
  maxDate = new Date(2024,1,1);
  ngOnInit() {
  }
} */

/*
export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
};
*/
@Component({
  selector: 'create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrls: ['./create-reservation.component.css'],
})
export class CreateReservationComponent implements OnInit {
  reservations!: Observable<Reservation[]>;
  reservation: Reservation = new Reservation();
  submitted = false;
  constructor(private reservationService: ReservationService) { }

  ngOnInit(): void {
  }

  newReservation(): void{
    this.submitted = false;
    this.reservation = new Reservation();
  }

  save() {
    this.reservation.dateReservation = this.reservation.dateReservation.toString();
    this.reservationService.createReservation(this.reservation)
      .subscribe(data => console.log(data), error => console.log(error));
    this.reservation = new Reservation();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  reloadData() {
    console.log(this.reservation.dateReservation.toString() + "  e  " + this.reservation.sportReservation)
    //this.reservations = this.reservationService.getReservationByDateAndSport(date, sport);
  }

}
