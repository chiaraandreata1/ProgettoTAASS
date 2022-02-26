import { Component, OnInit } from '@angular/core';

import {Reservation} from "../reservation";
import {ReservationService} from "../reservation.service";
import { DateAdapter } from '@angular/material/core';

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
  /*providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS, }
  ]*/
})
export class CreateReservationComponent implements OnInit {
  reservation: Reservation = new Reservation();
  submitted = false;
  constructor(private dateAdapter: DateAdapter<any>, private reservationService: ReservationService) { }

  ngOnInit(): void {
  }

  newReservation(): void{
    this.submitted = false;
    this.reservation = new Reservation();
  }

  engLocale() {
    this.dateAdapter.setLocale('en-US');
  }

  /*formatDate(date) {
    var d = new Date(date),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');}*/


  save() {
    this.reservationService.createReservation(this.reservation)
      .subscribe(data => console.log(data), error => console.log(error));
    this.reservation = new Reservation();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

}
