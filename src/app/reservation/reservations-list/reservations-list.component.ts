import { Component, OnInit } from '@angular/core';
import {Observable, Subscription} from 'rxjs';

import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';
import {OldUserService} from "../../services/user.service";
import {FormControl} from "@angular/forms";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.css']
})
export class ReservationsListComponent implements OnInit {

  reservations!: Observable<Reservation[]>;
  reservationsAdmin = new Array();

  dateReservation = new FormControl();
  sportReservation = '';

  isAdmin = false;

  courts = new Array();
  HoursAvailable = [8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
  monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  MonthsAvailable = new Array();

  subscription = new Subscription()

  minDate: Date;

  constructor(private reservationService: ReservationService, private userService: OldUserService, private NewUserService: UserService) {
    this.minDate = new Date();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.reloadData();
    const firstMonthIndex = new Date().getMonth()
    for (let i=firstMonthIndex; i<firstMonthIndex+6; i++)
      this.MonthsAvailable.push(this.monthNames[i]);
    this.subscription = this.NewUserService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  deleteReservations() {
    this.reservationService.deleteAllReservations()
      .subscribe(
        data => {
          console.log(data);
          //this.reservationsAdmin = new Array();
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    let date = new Date(this.dateReservation.value);
    let d = date.getDate();
    let m = date.getMonth() + 1;
    let y = date.getFullYear();
    let stringDate = `${d < 10 ? '0' : ''}${d}-${m < 10 ? '0' : ''}${m}-${y}`;
    if (this.sportReservation != "") //1970-01-01 è la data default se non scegli una data
    {
      if (!this.isAdmin)
        this.reservations = this.reservationService.getUserLoggedReservationsBySport(0, this.sportReservation); //TODO: userLoggedId deve essere l'id dell'utente loggato
      else if (stringDate!='01-01-1970')
        this.reservations = this.reservationService.getReservationByDateAndSport(stringDate, this.sportReservation);

        /*
        this.reservationService.getReservationByDateAndSport(stringDate, this.sportReservation).toPromise()
            .then(
                data => {
                    let reservationsObs = <Array<Reservation>>data;
                    console.log(reservationsObs)
                    let i = 0, arrRow = new Array(), hour = 8;
                    while (hour < 24 && i<reservationsObs.length) {
                        console.log(hour); console.log(i);
                        if (hour == reservationsObs[i].date.getHours())
                        {
                            arrRow.push(reservationsObs[i]);  i++;
                        }
                        else {
                            if (arrRow.length!=0) {
                                this.reservationsAdmin.push(arrRow);
                                arrRow = new Array();
                            }
                            hour++;
                        }
                        if (i==reservationsObs.length && arrRow.length!=0) this.reservationsAdmin.push(arrRow);
                    }
                })

         */
    }

  }

  debugBut() {
      console.log(this.sportReservation)
  }

}
