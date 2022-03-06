import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";

import { HttpClientModule } from "@angular/common/http";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReservationsListComponent } from './reservation/reservations-list/reservations-list.component';
import { ReservationDetailsComponent } from './reservation/reservation-details/reservation-details.component';
import { CreateBoardComponent } from './board/create-board/create-board.component';
import { BoardsListComponent } from './board/boards-list/boards-list.component';
import { BoardDetailsComponent } from './board/board-details/board-details.component';


import { MatButtonModule } from "@angular/material/button";
import { CreateReservationComponent } from './reservation/create-reservation/create-reservation.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DateAdapter, MAT_DATE_FORMATS, MatNativeDateModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { BoardMainComponent } from './board/board-main/board-main.component';
import { ReservationMainComponent } from './reservation/reservation-main/reservation-main.component';
import { BoardPersonalComponent } from './board/board-personal/board-personal.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatAutocompleteModule} from "@angular/material/autocomplete";

@NgModule({
  declarations: [
    AppComponent,
    ReservationsListComponent,
    ReservationDetailsComponent,
    CreateReservationComponent,
    CreateBoardComponent,
    BoardsListComponent,
    BoardDetailsComponent,
    BoardMainComponent,
    ReservationMainComponent,
    BoardPersonalComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    MatButtonModule,
    BrowserAnimationsModule,
    MatNativeDateModule,
    MatInputModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatAutocompleteModule,
  ],
  //crea correttamente la data, ma mantiene anche le informazioni non utili (ad esempio l'ora)
  providers: [
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE,
        MAT_MOMENT_DATE_ADAPTER_OPTIONS] },
    { provide: MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
    {
      provide: MAT_DATE_FORMATS, useValue: {
        parse: {
          dateInput: "L",
        },
        display: {
          dateInput: "L",
          monthYearLabel: "MMM YYYY",
          dateA11yLabel: "LL",
          monthYearA11yLabel: "MMMM YYYY",
        },
      }
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
