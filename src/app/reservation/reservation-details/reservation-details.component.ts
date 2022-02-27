import { Component, OnInit, Input } from '@angular/core';
import { ReservationService } from "../../services/reservation.service";
import { Reservation } from "../../models/reservation";

import {ReservationsListComponent} from "../reservations-list/reservations-list.component";

@Component({
  selector: 'reservation-details',
  templateUrl: './reservation-details.component.html',
  styleUrls: ['./reservation-details.component.css']
})
export class ReservationDetailsComponent implements OnInit {

  @Input() reservation!: Reservation;

  constructor(private reservationService: ReservationService, private listComponent: ReservationsListComponent) { }

  ngOnInit(): void {
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
