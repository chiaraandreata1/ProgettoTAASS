import { Component, OnInit } from '@angular/core';

import {Reservation} from "../../models/reservation";
import {ReservationService} from "../../services/reservation.service";
import {UserService} from "../../services/user.service";
import {map, Observable} from "rxjs";


import {AbstractControl, FormControl, FormGroup} from "@angular/forms";
import {Court} from "../../models/court";
import {User} from "../../models/user";

@Component({
  selector: 'create-reservation',
  templateUrl: './create-reservation.component.html',
  styleUrls: ['./create-reservation.component.css'],
})
export class CreateReservationComponent implements OnInit {
  reservation: Reservation = new Reservation();

  courts!: Observable<Court[]> //i campi disponibili del centro sportivo

  //info generali
  sportReservation = '';
  numberplayers = 0;

  //FormControls per ottenere gli users player
  playersForm = new FormGroup({
    player1: new FormControl('', [this.validatePlayer]),
    player2: new FormControl('', [this.validatePlayer]),
    player3: new FormControl('', [this.validatePlayer]),
    player4: new FormControl('', [this.validatePlayer]),
  });
  arrplayers = ['player1', 'player2', 'player3', 'player4'];

  //per creare i buttons con le prenotazioni possibili
  dateReservation = new FormControl();
  reservationsNOTAvailable!: Observable<Reservation[]>;
  hoursAvailable = [9,10,11,12,13,14,15,16,17,18,19,20,21];
  arrNOTAvailableForHours = new Array();

  //gestione del filtro delle opzioni
  filtersOptions = new Array();
  users = new Array();
  usersNotSelected = new Array();

  listplayersready = false; //TRUE quando abbiamo 4 players che sono 4 users diversi. Fa vedere il date picker
  searchready = false; //TRUE dopo aver selezionato una data. Fa vedere i bottoni delle prenotazioni
  submitted = false;

  minDate: Date;
  maxDate: Date;

  constructor(private reservationService: ReservationService, private  userService: UserService) {
  this.minDate = new Date();
  this.maxDate = new Date();
  this.maxDate.setMonth(this.minDate.getMonth()+1)
}

  ngOnInit(): void {
    this.courts = this.reservationService.getCourtsList();
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

  //modifica le filtered options con i nuovi usersNotSelected, che sono tutte uguali qualunque sia il player scelto
  changeFilters(){
    console.log('call changeFilters');
    let usersSelected = new Array();
    for (let i = 0; i<this.arrplayers.length; i++)
    {
      if (typeof this.playersForm.controls[this.arrplayers[i]].value != "string")
        usersSelected.push(this.playersForm.controls[this.arrplayers[i]].value);
    }
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
    this.numberplayers = 0;
    this.sportReservation = '';
    this.resetInputPlayers();
    this.reservation = new Reservation();
  }

  save() {
    this.reservationService.createReservation(this.reservation)
      .subscribe(data => console.log(data), error => console.log(error));
    this.reservation = new Reservation();
  }

  createReservation(sportReservation: string, dateReservation: string, hour: number, courtId: number, courtType: string)
  {
    //si potrebbe magari scrivere meglio l'assegnamento ai 4 players
    this.reservation.player1 = typeof this.playersForm.controls[this.arrplayers[0]].value == "string" ? this.playersForm.controls[this.arrplayers[0]].value : this.playersForm.controls[this.arrplayers[0]].value.username;
    this.reservation.player2 = typeof this.playersForm.controls[this.arrplayers[1]].value == "string" ? this.playersForm.controls[this.arrplayers[1]].value : this.playersForm.controls[this.arrplayers[1]].value.username;
    if (this.numberplayers>2)
    {
      this.reservation.player3 = typeof this.playersForm.controls[this.arrplayers[2]].value == "string" ? this.playersForm.controls[this.arrplayers[2]].value : this.playersForm.controls[this.arrplayers[2]].value.username;
      this.reservation.player4 = typeof this.playersForm.controls[this.arrplayers[3]].value == "string" ? this.playersForm.controls[this.arrplayers[3]].value : this.playersForm.controls[this.arrplayers[3]].value.username;
    }
    this.reservation.sportReservation = sportReservation;
    this.reservation.dateReservation = new Date(this.reservation.dateReservation.toString()).toISOString().split('T')[0];
    this.reservation.hourReservation = hour;
    let court = new Court();
    court.id = courtId;
    court.type = courtType;
    this.reservation.courtReservation = court;
    this.submitted = true;
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
    console.log('call enableInputPlayer');
    absControl.reset();
    absControl.enable();
    this.listplayersready = false;
    this.changeFilters();
  }

  //si attiva quando hai scelto l'user player. Disabilita la label
  disableInputPlayer(absControl: AbstractControl)
  {
    console.log('call enableInputPlayer');
    absControl.disable();
    this.changeFilters();
  }

  //resetta e pulisce gli input dei 4 player. Si attiva quando cambi i campi sport, numero giocatori o quando fai una nuova prenotazione
  resetInputPlayers(){
    console.log('call resetInputPlayers');
    console.log('numero giocatori = ' + this.numberplayers);
    for (let i = 0; i<this.arrplayers.length; i++)
    {
      console.log('ciclo ' + i);
      if (i<this.numberplayers && (typeof this.playersForm.controls[this.arrplayers[i]].value != 'string' || !this.playersForm.controls[this.arrplayers[i]].value))
      {
        console.log('if');
        this.playersForm.controls[this.arrplayers[i]].reset();
        this.playersForm.controls[this.arrplayers[i]].enable();
      }
      else
      {
        console.log('else');
        this.playersForm.controls[this.arrplayers[i]].reset();
        this.playersForm.controls[this.arrplayers[i]].disable();
      }

    }
    this.changeFilters();
  }

  //funzione che evita di selezionare il numero di giocatori se scegli padel come sport
  checkRadioInputs() {
    console.log('call checkRadioInputs');
    if (this.sportReservation == 'padel')
      this.numberplayers = 4;
    else if (this.sportReservation == 'tennis')
      this.numberplayers = 0;
    this.resetInputPlayers();
  }

  //setta il flag true quando tutti i campi input (da player 1 a 4) conterranno le classi user corrette, e permette di scegliere la data
  playerlistready() {
    for (let i = 0; i<this.numberplayers; i++)
    {
      if (typeof this.playersForm.controls[this.arrplayers[i]].value == "string" || !this.playersForm.controls[this.arrplayers[i]].value) {
        this.listplayersready = false;
        return;
      }
    }
    this.listplayersready = true;
  }

  //funzione che entra in gioco quando i campi precedenti sono tutti corretti, e permette di scegliere una data. La reservationsNOTAvailable è il complementare che crea poi dall'html le reservation disponibili
  reloadData() {
    console.log('call reloadData');
    if (this.dateReservation.value!=undefined && this.sportReservation!='')
    {
      this.searchready = true;
      this.reservationsNOTAvailable = this.reservationService.getReservationByDateAndSport(new Date(this.dateReservation.value).toISOString().split('T')[0], this.sportReservation)
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
              if (reservationsNOTAvailableNOTobs[j].hourReservation == this.hoursAvailable[i])
                arr.push(reservationsNOTAvailableNOTobs[j].courtReservation.id);
            }
            this.arrNOTAvailableForHours.push(arr);
          }
        }
      );
    }
    else
      this.searchready = false;
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

//bottone debugging
  show() {

  }
}

