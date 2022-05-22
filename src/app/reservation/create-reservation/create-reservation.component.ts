import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {UserService} from "../../services/user.service";
import {map, Observable} from "rxjs";


import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {DummyCourt} from "../../models/dummyCourt";
import {User} from "../../models/user";

interface TypeMatchTennis {
  numberplayers: number;
  type: string;
}

@Component({
  selector: 'create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrls: ['./create-reservation.component.css'],
})
export class CreateReservationComponent implements OnInit {
  reservation!: Reservation;

  courts!: Observable<DummyCourt[]> //i campi disponibili del centro sportivo

  OptionsTennis: TypeMatchTennis[] = [
    {numberplayers: 2, type: 'Single'},
    {numberplayers: 4, type: 'Double'},
  ]

  //info generali
  userLogged = '';
  isAdmin = false;
  sportReservation = '';
  numberplayers = 0;

  rows=4;
  cols=4;

  //FormControls per ottenere gli users player
  playersForm = new FormGroup({});
  arrplayers = new Array();

  //per creare i buttons con le prenotazioni possibili
  dateReservation = new FormControl();
  reservationsNOTAvailable!: Observable<Reservation[]>;
  hoursAvailable = [8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];
  arrNOTAvailableForHours = new Array();

  //gestione del filtro delle opzioni
  filtersOptions = new Array();
  users = new Array();
  usersNotSelected = new Array();

  listplayersready = false; //TRUE quando abbiamo 4 players che sono 4 users diversi. Fa vedere il date picker
  searchready = false; //TRUE dopo aver selezionato una data. Fa vedere i bottoni delle prenotazioni
  submitted = false;

  arrALLReservationsOrdByHourAndCourt = new Array();

  minDate: Date;
  maxDate: Date;

  constructor(private reservationService: ReservationService, private  userService: UserService) {
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

  ngOnInit(): void {
    this.isAdmin = this.userService.getRoleUserLogged() == "admin";
    this.userLogged = this.userService.getUserLogged();
    this.arrplayers = this.isAdmin ? ['player1', 'player2', 'player3', 'player4'] : ['player2', 'player3', 'player4'];
    for (let i = 0; i<this.arrplayers.length; i++)
      this.playersForm.addControl(this.arrplayers[i], new FormControl('', [this.validatePlayer]));
    //INIZIALIZZO IL FILTRO SUGGERIMENTI, dove inizialmente la usersNotSelected comprende tutti gli utenti
    this.prepareUserOptions();
    for (let i = 0; i<this.arrplayers.length; i++)
    {
      let filteredOptions = this.playersForm.controls[this.arrplayers[i]].valueChanges.pipe(
        map(user => (typeof user === 'string' ? user : user.username)),
        map(user => (user ? this._filter(user) : this.usersNotSelected.slice())),
      );
      this.playersForm.controls[this.arrplayers[i]].disable();
      this.filtersOptions.push(filteredOptions)
    }
  }
  //TODO: il changefilters va fatto senza utilizzare la get all users (collegamento alla prepareuseroption di riga 99). Serve una get specifica
  //modifica le filtered options con i nuovi usersNotSelected, che sono tutte uguali qualunque sia il player scelto
  changeFilters(){
    let usersSelected = new Array();
    for (let i = 0; i<this.arrplayers.length; i++)
    {
      if (typeof this.playersForm.controls[this.arrplayers[i]].value != "string")
        usersSelected.push(this.playersForm.controls[this.arrplayers[i]].value);
    }
    if (!this.isAdmin)
      usersSelected.push(this.userLogged);
    this.usersNotSelected = this.users.filter(value => !usersSelected.includes(value));
    this.filtersOptions = new Array();
    for (let i = 0; i<this.arrplayers.length; i++) {
      let filteredOptions = this.playersForm.controls[this.arrplayers[i]].valueChanges.pipe(
        map(user => (typeof user === 'string' ? user : user.username)),
        map(user => (user ? this._filter(user) : this.usersNotSelected.slice())),
      );
      this.filtersOptions.push(filteredOptions)
    }
  }

  //CREAZIONE DELL'ARRAY USER E USERNAMES-------------------------------------------------
  //utilizzata dalla onInit, inizializza l'array observable di users e usersNotSelected, che inizialmente sono uguali
  //TODO: costruirlo in modo tale che vengano presi solo gli users richiesti prendendoli dal server (e non tutti) (collegamento alla changefilters di riga 75)
  prepareUserOptions(): void {
    let obsUsers = this.userService.getUsersList();
    obsUsers.toPromise()
      .then(
        data => {
          this.users = new Array();
          this.users = this.usersNotSelected = <Array<User>>data;
        }
      );
  }

  //queste ultime 3 funzioni servono a costruire correttamente le mat options per la barra dei suggerimenti
  displayFn(user: User): string {
    return user && user.username ? user.username : '';
  }

  private _filter(username: string): User[] {
    const filterValue = username.toLowerCase();
    return this.usersNotSelected.filter(users => users.username.toLowerCase().includes(filterValue));
  }

  validatePlayer(control: AbstractControl): {[key: string]: any} | null  {
    if (typeof control.value == 'string') {
      return { 'userInvalid': true };
    }
    return null;
  }
  //----------------------------------------------------------------------------------------
  //FUNZIONI PER MANIPOLARE LA CLASSE RESERVATION-------------------------------------------
  newReservation(): void{
    this.submitted = false;
    this.dateReservation.reset();
    this.numberplayers = 0;
    this.sportReservation = '';
    this.resetInputPlayers();
    //this.reservation = new Reservation();
  }

  save() {
    let body = Reservation.toJSON(this.reservation);
    this.reservationService.createReservation(body)
      .subscribe(data => console.log(data), error => console.log(error));
    //this.reservation = new Reservation();
    this.searchready = false;
  }

  createReservation(sportReservation: string, dateReservation: string, hour: number, courtId: number, courtType: string)
  {
    let players = new Array();
    let nPlayersToConsider = this.isAdmin ? this.numberplayers : this.numberplayers-1;
    if (!this.isAdmin)
      players.push(this.userService.getIdUserLogged());
    for (let i = 0; i<nPlayersToConsider; i++)
      players.push(typeof this.playersForm.controls[this.arrplayers[i]].value == "string" ? this.playersForm.controls[this.arrplayers[i]].value : this.playersForm.controls[this.arrplayers[i]].value.id);
    let date = new Date(this.dateReservation.value)
    date.setHours(hour)
    let court = new DummyCourt();
    court.id = courtId;
    court.type = courtType;
    this.reservation = new Reservation(-1, sportReservation, date, 'private', court, players);
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
  //si attiva quando vuoi svutare la label dall'user selezionato. Riabilita la label
  enableInputPlayer(absControl: AbstractControl)
  {
    absControl.setValue('');
    absControl.enable();
    this.listplayersready = false;
    this.changeFilters();
    this.reloadData();
  }

  //si attiva quando hai scelto l'user player. Disabilita la label
  disableInputPlayer(absControl: AbstractControl, playerdisabled: string)
  {
    absControl.disable();
    this.changeFilters();
    this.playerlistready(playerdisabled);
    this.reloadData();
  }

  //resetta e pulisce gli input dei 4 player. Si attiva quando cambi i campi sport, numero giocatori o quando fai una nuova prenotazione, vedere il submit button
  resetInputPlayers(){
    let nPlayersToConsider = this.isAdmin ? this.numberplayers : this.numberplayers-1;
    for (let i = 0; i<this.arrplayers.length; i++)
    {
      this.playersForm.controls[this.arrplayers[i]].reset();
      if (i<nPlayersToConsider && (typeof this.playersForm.controls[this.arrplayers[i]].value != 'string' || !this.playersForm.controls[this.arrplayers[i]].value))
        this.playersForm.controls[this.arrplayers[i]].enable();
      else
        this.playersForm.controls[this.arrplayers[i]].disable();
    }
    this.listplayersready = false;
    this.changeFilters();
    this.reloadData();
  }

  //funzione che evita di selezionare il numero di giocatori se scegli padel come sport (se scegli padel è implicito che siano 4 i giocatori)
  checkRadioInput() {
    if (this.sportReservation == 'padel')
      this.numberplayers = 4;
    else if (this.sportReservation == 'tennis')
      this.numberplayers = 0;
    this.resetInputPlayers();
  }

  //setta il flag true quando tutti i campi input (da player 1 a 4) conterranno le classi user corrette, e permette di scegliere la data
  playerlistready(playerdisabled: string) {
    let nPlayersToConsider = this.isAdmin ? this.numberplayers : this.numberplayers-1;
    for (let i = 0; i<nPlayersToConsider; i++)
    {
      if ((typeof this.playersForm.controls[this.arrplayers[i]].value == "string" || !this.playersForm.controls[this.arrplayers[i]].value) && playerdisabled!=this.arrplayers[i]) {
        this.listplayersready = false;
        return;
      }
    }
    this.listplayersready = true;
  }

  //funzione che entra in gioco quando i campi precedenti sono tutti corretti, e permette di scegliere una data. La reservationsNOTAvailable è il complementare che crea poi dall'html le reservation disponibili
  reloadData() {
    if (this.dateReservation.value!=undefined && this.sportReservation!='' && this.listplayersready)
    {
      this.searchready = true;
      this.arrNOTAvailableForHours = new Array();
      this.arrALLReservationsOrdByHourAndCourt = new Array();
      this.courts = this.reservationService.getCourtsListByType(this.sportReservation);
      //Preso dalla serialization--------------------- TODO:sostituire poi con la versione che prende anche gli slash
      let date = new Date(this.dateReservation.value);
      let d = date.getDate();
      let m = date.getMonth() + 1;
      let y = date.getFullYear();
      let stringDate = `${d < 10 ? '0' : ''}${d}-${m < 10 ? '0' : ''}${m}-${y}`;
      //----------------------------------------------
      this.reservationsNOTAvailable = this.reservationService.getReservationByDateAndSport(stringDate, this.sportReservation)
      this.reservationsNOTAvailable.toPromise()
      .then(
        data => {
          let reservationsNOTAvailableNOTobs = <Array<Reservation>>data;
          this.arrNOTAvailableForHours = new Array();
          for (let i=0; i<this.hoursAvailable.length; i++)
          {
            let arr = new Array();

            for (let j = 0; j<reservationsNOTAvailableNOTobs.length; j++)
            {
              if (reservationsNOTAvailableNOTobs[j].date.getHours() - 1 == this.hoursAvailable[i])
                arr.push(reservationsNOTAvailableNOTobs[j]);
            }
            this.arrNOTAvailableForHours.push(arr);
          }
          for (let hour = 0; hour<this.hoursAvailable.length; hour++)
          {
            let first_court = this.sportReservation == 'tennis' ? 1 : 4;
            let arr = new Array()
            for (let court_id = first_court; court_id<first_court+3; court_id++) {
              let reservation = this.arrNOTAvailableForHours[hour].find((reservation: Reservation) => reservation.courtReservation.id === court_id)
              arr.push(reservation);
            }
            this.arrALLReservationsOrdByHourAndCourt.push(arr);
          }
        }
      );
    }
    else
      this.searchready = false;
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
    console.log(this.numberplayers)
  }

  counter(i: number) {
    return new Array(i);
  }
}

