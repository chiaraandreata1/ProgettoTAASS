import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";

import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
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
import {MatIconModule} from "@angular/material/icon";
import {MatRadioModule} from "@angular/material/radio";
import { TournamentsMainComponent } from './tournaments/tournaments-main/tournaments-main.component';
import {NgTournamentTreeModule} from "ng-tournament-tree";
import { CreateTournamentComponent } from './tournaments/create-tournament/create-tournament.component';
import { CreateTeamComponent } from './shared/team/create-team/create-team.component';
import { TeamCardComponent } from './shared/team/team-card/team-card.component';
import { SportSelectorComponent } from './shared/sports/sport-selector/sport-selector.component';
import {ForLoopPipe} from "./utilities/for-loop-pipe";
import { DatesComponent } from './shared/misc/dates/dates.component';
import { CreateTeamListComponent } from './shared/team/create-team-list/create-team-list.component';
import {JsonInterceptor, JsonParser} from "./utilities/json-interceptor";
import {CustomJsonParser} from "./CustomJsonParser";
import {Serialization} from "./utilities/serialization";
import { TournamentViewComponent } from './tournaments/tournament-view/tournament-view.component';
import { ShowTournamentComponent } from './tournaments/show-tournament/show-tournament.component';
import { CourseMainComponent } from './course/course-main/course-main.component';
import { CoursesListComponent } from './course/courses-list/courses-list.component';
import { CourseDetailsComponent } from './course/course-details/course-details.component';
import { CreateCourseComponent } from './course/create-course/create-course.component';

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
    TournamentsMainComponent,
    CreateTournamentComponent,
    CreateTeamComponent,
    TeamCardComponent,
    SportSelectorComponent,
    ForLoopPipe,
    DatesComponent,
    CreateTeamListComponent,
    TournamentViewComponent,
    ShowTournamentComponent,
    CourseMainComponent,
    CoursesListComponent,
    CourseDetailsComponent,
    CreateCourseComponent,
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
    MatIconModule,
    MatRadioModule,
    NgTournamentTreeModule
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
    },
    { provide: HTTP_INTERCEPTORS, useClass: JsonInterceptor, multi: true },
    { provide: JsonParser, useClass: CustomJsonParser },
    { provide: Serialization, useClass: Serialization},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
