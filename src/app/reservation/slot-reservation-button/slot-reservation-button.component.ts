import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Reservation} from "../../models/reservation";
import {Serialization} from "../../utilities/serialization";
import {UserService} from "../../user/user.service";
import {ReservationService} from "../../services/reservation.service";
import {Team} from "../../models/tournament";
import {UserInfo} from "../../models/user-info";
import {Observable} from "rxjs";

@Component({
  selector: 'app-slot-reservation-button',
  templateUrl: './slot-reservation-button.component.html',
  styleUrls: ['./slot-reservation-button.component.css']
})
export class SlotReservationButtonComponent implements OnInit {

  @Input() reservationInput!: Reservation;
  @Input() hour!: number;
  @Input() court!: number;
  @Input() allPlayers!: Team;
  @Input() isAdmin!: boolean;
  @Input() userID!: number;
  @Input() minDate!: Date;
  @Input() dateToReserve!: Date;
  @Input() sportToReserve!: number;
  users = new Array()
  finalReservation!: Reservation;

  reservationPlayersString = '('

  @Output() done: EventEmitter<void> = new EventEmitter<void>();
  @Output() EventUserInfos = new EventEmitter<Observable<UserInfo[]>>()
  @Output() EventReservetionOutput = new EventEmitter<Reservation>()

  constructor(private userService: UserService, private reservationService: ReservationService ) {}

  ngOnInit(): void {
    if (this.reservationInput && this.reservationInput.players.length>0)
      this.userService.getUsers(this.reservationInput.players).toPromise().then( data => {
        let usersInput =  <Array<UserInfo>>data;
        for (let i = 0; i<usersInput.length; i++){
          if (i+1<usersInput.length)
            this.reservationPlayersString+=usersInput[i].displayName + '-';
          else this.reservationPlayersString+=usersInput[i].displayName+')';
        }
      })
  }

  save() {
    let body = Reservation.toJSON(this.finalReservation);
    this.reservationService.createReservation(body)
      .subscribe(data => console.log(data), error => console.log(error));
    this.done.emit()
    let users = this.userService.getUsers(this.finalReservation.players)
    this.EventUserInfos.emit(users)
    this.EventReservetionOutput.emit(this.finalReservation);
  }

  createReservation()
  {
    let players = this.allPlayers.players;
    if (!this.isAdmin)
      players.push(this.userID);
    let date = new Date(this.dateToReserve)
    date.setHours(this.hour)
    let finalStringDate = Serialization.serializeDateTime(date);
    this.finalReservation = new Reservation(-1, this.userID, this.sportToReserve, finalStringDate, 1, 'USER', this.court, players);
    this.save();
  }

  //funzione utile soprattutto se si vuole prenotare il giorno stesso. Le ore del giorno stesso già passate devono avere i bottoni disabilitati.
  checkAvailableDate(hour: number): boolean {
    if (this.minDate.getDay()==new Date().getDay()) //controllo da effettuare soltanto se prenoti lo stesso giorno. Le ore dei giorni dopo vanno sempre bene
    {
      let completeDate = new Date(this.dateToReserve); completeDate.setHours(hour);
      return completeDate > this.minDate;
    }
    else return true;
  }

}
