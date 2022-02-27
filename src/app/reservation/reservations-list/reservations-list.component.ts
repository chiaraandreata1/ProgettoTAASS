import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';


@Component({
  selector: 'reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.css']
})
export class ReservationsListComponent implements OnInit {

  reservations!: Observable<Reservation[]>;

  constructor(private reservationService: ReservationService) { }

  ngOnInit(): void {
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
    this.reservations = this.reservationService.getReservationsList();
  }

}
