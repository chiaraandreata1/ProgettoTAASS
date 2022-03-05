import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {Observable, Subscription} from "rxjs";


import {FormControl} from "@angular/forms";
import {Court} from "../../models/court";

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
  hoursAvailable = [9,10,11,12,13,14,15,16,17,18,19,20,21];
  submitted = false;
  arrNOTAvailableForHours = new Array();
  subscription = new Subscription();

  minDate: Date;

  constructor(private reservationService: ReservationService) {
  this.minDate = new Date();
}

  ngOnInit(): void {
    this.courts = this.reservationService.getCourtsList();
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
    this.save();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  reloadData() {
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

  deleteReservation(id: number) {
    this.reservationService.deleteReservation(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  showarray(){
    console.log(this.arrNOTAvailableForHours)
  }
}


