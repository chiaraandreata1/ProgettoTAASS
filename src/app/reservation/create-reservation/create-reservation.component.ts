import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {Observable, Subscription} from "rxjs";


import {FormControl} from "@angular/forms";
import {DummyCourt} from "../../models/dummyCourt";
import {FacilityService} from "../../services/facility.service";
import {UserService} from "../../user/user.service";
import {Serialization} from "../../utilities/serialization";
import {Team} from "../../models/tournament";
import {Court} from "../../models/court";

interface TypeMatchTennis {
  idsport: number,
  type: string;
}

@Component({
  selector: 'create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrls: ['./create-reservation.component.css'],
})
export class CreateReservationComponent implements OnInit {
  reservation!: Reservation;

  courtsTennis = new Array(); courtsPadel = new Array();
  courtsReservation = new Array();

  OptionsTennis: TypeMatchTennis[] = [
    {idsport: 2, type: 'Single'},
    {idsport: 3, type: 'Double'},
  ]

  //info generali
  userID: number;
  isAdmin = false;
  subscription = new Subscription();

  allSports = new Array()
  sportReservation = 0;
  numberplayers = 0;
  isTennis = new FormControl();

  allPlayers!: Team;

  //per creare i buttons con le prenotazioni possibili
  dateReservation = new FormControl();
  reservationsNOTAvailable!: Observable<Reservation[]>;
  hoursAvailable = [8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];
  arrNOTAvailableForHours = new Array();

  listplayersready = false; //TRUE quando abbiamo 4 players che sono 4 users diversi. Fa vedere il date picker
  searchready = false; //TRUE dopo aver selezionato una data. Fa vedere i bottoni delle prenotazioni
  submitted = false;

  arrALLReservationsOrdByHourAndCourt = new Array();

  minDate: Date;
  maxDate: Date;

  constructor(private reservationService: ReservationService, private facilityService: FacilityService, private userService: UserService) {
    let id:number = this.userService.getCurrentUser()?.id || 0;
    this.userID=id;
    let date = new Date()
    if (date.getHours()<23)
      this.minDate = date;
    else {
      date.setDate(date.getDate()+1)
      this.minDate = date;
    }
    this.maxDate = new Date();
    this.maxDate.setDate(new Date().getDate()+13);
}

  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = this.userService.isAdmin().subscribe(data => {this.isAdmin = data;});
    this.facilityService.getCourts().toPromise().then(data => {
      let allCourts = <Array<Court>>data;
      for (let i = 0; i<allCourts.length; i++)
        if (allCourts[i].sport.name=="Tennis") this.courtsTennis.push(allCourts[i].id);
        else this.courtsPadel.push(allCourts[i].id)
    })
  }

  //----------------------------------------------------------------------------------------
  //FUNZIONI PER MANIPOLARE LA CLASSE RESERVATION-------------------------------------------
  newReservation(): void{
    this.submitted = false;
    this.dateReservation.reset();
    this.numberplayers = 0;
  }

  save() {
    let body = Reservation.toJSON(this.reservation);
    this.reservationService.createReservation(body)
      .subscribe(data => console.log(data), error => console.log(error));
    this.searchready = false;
  }

  createReservation(sportReservation: number, dateReservation: string, hour: number, courtId: number)
  {
    let players = this.allPlayers.players;
    if (!this.isAdmin)
      players.push(this.userID);
    let date = new Date(this.dateReservation.value)
    date.setHours(hour)
    let finalStringDate = Serialization.serializeDateTime(date); //TODO TOGLIERE QUANDO RI FUNZIONA IL SERIALIZZATORE
    let court = new DummyCourt();
    court.id = courtId;
    this.reservation = new Reservation(-1, this.userID, sportReservation, finalStringDate, 1, 'USER', court.id, players);
    this.submitted = true;
    console.log(this.reservation);
    this.save();
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

  //----------------------------------------------------------------------------------------
  //GESTIONE DINAMICA DEL SERVIZIO----------------------------------------------------------
  //funzione che evita di selezionare il numero di giocatori se scegli padel come sport (se scegli padel è implicito che siano 4 i giocatori)
  checkRadioInput() {
    if (!this.isTennis.value) {
      this.numberplayers = this.sportReservation = 4;
      if (!this.isAdmin) this.sportReservation = this.sportReservation-1;
      this.courtsReservation = this.courtsPadel;
    }
    else {
      this.numberplayers = this.sportReservation = 0;
      this.courtsReservation = this.courtsTennis;
    }
  }

  setNumPlayers(){
    let playerconsidered = this.isAdmin ? 0 : 1;
    if (this.isTennis)
      if (this.sportReservation==2)
        this.numberplayers=2-playerconsidered;
      else if (this.sportReservation==3)
        this.numberplayers=4-playerconsidered;
    this.reloadData();
  }

  //funzione che entra in gioco quando i campi precedenti sono tutti corretti, e permette di scegliere una data. La reservationsNOTAvailable è il complementare che crea poi dall'html le reservation disponibili
  reloadData() {
    this.searchready = false;
    if (this.dateReservation.value!=undefined && this.sportReservation>1 && this.listplayersready && this.isTennis.value)
    {
      this.arrNOTAvailableForHours = new Array();
      this.arrALLReservationsOrdByHourAndCourt = new Array();
      //Preso dalla serialization--------------------- TODO:sostituire poi con la versione che prende anche gli slash
      let date = new Date(this.dateReservation.value);
      let d = date.getDate();
      let m = date.getMonth() + 1;
      let y = date.getFullYear();
      let stringDate = `${d < 10 ? '0' : ''}${d}-${m < 10 ? '0' : ''}${m}-${y}`;
      //----------------------------------------------
      console.log('doing the GET operation')
      this.reservationsNOTAvailable = this.reservationService.getReservationByDateAndSportIsTennis(stringDate, this.isTennis.value)
      this.reservationsNOTAvailable.toPromise()
      .then(
        data => {
          console.log(data);
          let reservationsNOTAvailableNOTobs = <Array<Reservation>>data;
          this.arrNOTAvailableForHours = new Array();
          for (let i=0; i<this.hoursAvailable.length; i++)
          {
            let arr = new Array();

            for (let j = 0; j<reservationsNOTAvailableNOTobs.length; j++)
            {
              if (Serialization.deserializeDate(reservationsNOTAvailableNOTobs[j].date).getHours() - 1 == this.hoursAvailable[i]) //TODO: risistemare quando verrà serializzato reservationsNOTAvailableNOTobs[j].date.getHours() - 1 == this.hoursAvailable[i]
                arr.push(reservationsNOTAvailableNOTobs[j]);
            }
            this.arrNOTAvailableForHours.push(arr);
          }
          for (let hour = 0; hour<this.hoursAvailable.length; hour++)
          {
            let tennisSportsId = [1,2,3]
            let first_court = tennisSportsId.includes(this.sportReservation) ? 5 : 8;
            let arr = new Array()
            for (let court_id = first_court; court_id<first_court+3; court_id++) {
              let reservation = this.arrNOTAvailableForHours[hour].find((reservation: Reservation) => reservation.courtReserved === court_id)
              arr.push(reservation);
            }
            this.arrALLReservationsOrdByHourAndCourt.push(arr);
          }
          this.searchready = true;
        }
      );
    }
  }

  //funzione utile soprattutto se si vuole prenotare il giorno stesso. Le ore del giorno stesso già passate devono avere i bottoni disabilitati.
  checkAvailableDate(hour: number): boolean {
    if (this.minDate.getDay()==new Date().getDay()) //controllo da effettuare soltanto se prenoti lo stesso giorno. Le ore dei giorni dopo vanno sempre bene
    {
      let completeDate = new Date(this.dateReservation.value); completeDate.setHours(hour);
      return completeDate > this.minDate;
    }
    else return true;
  }

  //la save la fa la createReservation che prende tutti i valori necessari
  onSubmit() {
    this.submitted = true;
  }

//bottone debugging
  show() {
    console.log(this.arrALLReservationsOrdByHourAndCourt)
  }
}

