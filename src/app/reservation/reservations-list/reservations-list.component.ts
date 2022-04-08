import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';
import {UserService} from "../../services/user.service";
import {FormControl} from "@angular/forms";


@Component({
  selector: 'reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.css']
})
export class ReservationsListComponent implements OnInit {

  reservations!: Observable<Reservation[]>;

  dateReservation = new FormControl();
  sportReservation = new FormControl();

  isAdmin = false;

  minDate: Date;
  maxDate: Date;

  constructor(private reservationService: ReservationService, private userService: UserService) {
    this.minDate = new Date();
    this.maxDate = new Date();
    this.maxDate.setMonth(this.minDate.getMonth()+1);
  }

  ngOnInit(): void {
    this.isAdmin = this.userService.getRoleUserLogged() == "admin";
    this.reloadData();
  }

  deleteReservations() {
    this.reservationService.deleteAllReservations()
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    let date = new Date(this.dateReservation.value).toISOString().split('T')[0];
    if (date!='1970-01-01' && typeof this.sportReservation == "string") //1970-01-01 è la data default se non scegli una data
      this.reservations = this.isAdmin ?
        this.reservationService.getReservationByDateAndSport(date, this.sportReservation) :
        this.reservationService.getUserLoggedReservationsByDateAndSport(this.userService.getUserLogged(), date, this.sportReservation);
  }

}
