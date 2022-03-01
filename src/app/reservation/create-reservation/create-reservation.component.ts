import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {Observable} from "rxjs";

import * as $ from 'jquery';
import {FormControl} from "@angular/forms";

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
  sportReservation = new FormControl();
  dateReservation = new FormControl();
  submitted = false;
  constructor(private reservationService: ReservationService) { }

  ngOnInit(): void {
  }

  newReservation(): void{
    this.submitted = false;
    this.reservation = new Reservation();
  }

  save() {
    this.reservation.dateReservation = new Date(this.reservation.dateReservation.toString()).toISOString().split('T')[0];
    this.reservationService.createReservation(this.reservation)
      .subscribe(data => console.log(data), error => console.log(error));
    this.reservation = new Reservation();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  reloadData() {
    this.reservation.dateReservation = new Date(this.reservation.dateReservation.toString()).toISOString().split('T')[0];
    console.log(this.dateReservation.value.toISOString().split('T')[0])
    this.reservations = this.reservationService.getReservationByDateAndSport(this.dateReservation.value.toISOString().split('T')[0], this.sportReservation.value);
  }

}


