import {Component, Injectable, OnInit} from '@angular/core';
import {Observable, Subscription} from 'rxjs';

import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';
import {FormControl} from "@angular/forms";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class ReservationsListComponent implements OnInit {

  reservationsUser!: Observable<Reservation[]>;
  reservationsForHour = new Array()
  reservationsAdmin = new Array();

  dateReservation = new FormControl();
  sportReservation = 0; //2 TENNIS 4 PADEL

  isAdmin = false;

  HoursAvailable = [8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
  monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  MonthsAvailable = new Array();

  subscription = new Subscription()

  minDate: Date;

  userID=0;

  constructor(private reservationService: ReservationService, private userService: UserService) {
    this.minDate = new Date();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    let id:number = this.userService.getCurrentUser()?.id || 0;
    this.userID=id;

    this.reloadData();
    const firstMonthIndex = new Date().getMonth()
    for (let i=firstMonthIndex; i<firstMonthIndex+6; i++)
      this.MonthsAvailable.push(this.monthNames[i]);
    this.subscription = this.userService.isAdmin().subscribe(data => { this.isAdmin = data; });
  }

  reloadData() {
    this.reservationsForHour = new Array();
    let date = new Date(this.dateReservation.value);
    let d = date.getDate();
    let m = date.getMonth() + 1;
    let y = date.getFullYear();
    let stringDate = `${d < 10 ? '0' : ''}${d}-${m < 10 ? '0' : ''}${m}-${y}`;
    if (this.sportReservation != 0) //1970-01-01 è la data default se non scegli una data
    {
      if (!this.isAdmin)
        this.reservationsUser = this.reservationService.getUserLoggedReservationsBySport(this.userID, this.sportReservation);
      else if (stringDate!='01-01-1970')
      {
        for (let i = 0; i<this.HoursAvailable.length; i++)
            this.reservationsForHour.push(this.reservationService.getReservationByDateAndSportAndHour(stringDate, this.sportReservation, this.HoursAvailable[i]));
      }
    }

  }

  debugBut() {
      console.log(this.reservationsForHour)
  }

}
