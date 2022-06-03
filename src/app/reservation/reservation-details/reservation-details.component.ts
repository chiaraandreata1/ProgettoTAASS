import { Component, OnInit, Input } from '@angular/core';
import { ReservationService } from "../../services/reservation.service";
import { Reservation } from "../../models/reservation";

import {ReservationsListComponent} from "../reservations-list/reservations-list.component";
import {UserService} from "../../user/user.service";
import {UserInfo} from "../../models/user-info";

@Component({
  selector: 'reservation-details',
  templateUrl: './reservation-details.component.html',
  styleUrls: ['./reservation-details.component.css']
})
export class ReservationDetailsComponent implements OnInit {

  @Input() reservation!: Reservation;
  @Input() isAdmin!: boolean;

  playersString = '';

  isLoggedReservation = false;

  constructor(private reservationService: ReservationService, private listComponent: ReservationsListComponent, private userService: UserService) { }

  ngOnInit(): void {
    let id:number = this.userService.getCurrentUser()?.id || 0;
    if (this.reservation.players.includes(id))
      this.isLoggedReservation = true;
    if (this.reservation.players.length>0)
      this.userService.getUsers(this.reservation.players).toPromise().then(data => {
        let listPlayers = <Array<UserInfo>>data;
        for (let i = 0; i<listPlayers.length; i++)
          if (i+1<listPlayers.length)
            this.playersString += listPlayers[i].displayName + ', '
          else
            this.playersString += listPlayers[i].displayName;
      });

  }

  deleteReservation() {
    this.reservationService.deleteReservation(this.reservation.id)
      .subscribe(
        data => {
          console.log(data);
          this.listComponent.reloadData();
        },
        error => console.log(error));
  }
}
