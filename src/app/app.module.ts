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
import {MatNativeDateModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
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
import {ForLoopPipe, ForTPLoop} from "./utilities/for-loop-pipe";
import { DatesComponent } from './shared/misc/dates/dates.component';
import { CreateTeamListComponent } from './shared/team/create-team-list/create-team-list.component';
import {JsonInterceptor, JsonParser} from "./utilities/json-interceptor";
import {CustomJsonParser} from "./custom-json-parser";
import {Serialization} from "./utilities/serialization";
import { TournamentViewComponent } from './tournaments/tournament-view/tournament-view.component';
import { ShowTournamentComponent } from './tournaments/show-tournament/show-tournament.component';
import { CourseMainComponent } from './course/course-main/course-main.component';
import { CourseDetailsComponent } from './course/course-details/course-details.component';
import { CreateCourseComponent } from './course/create-course/create-course.component';
// import { UserDetailsComponent } from './user-details/user-details.component';
import {MatSelectModule} from "@angular/material/select";
import { CoursesCompletedComponent } from './course/courses-completed/courses-completed.component';
import { CoursesPendingComponent } from './course/courses-pending/courses-pending.component';
import {MatTabsModule} from "@angular/material/tabs";
import {MatCardModule} from "@angular/material/card";
import {MatDividerModule} from "@angular/material/divider";
import { LoginComponent } from './user/login/login.component';
import {authInterceptorProviders} from "./utilities/auth-interceptor";
import { UserBadgeComponent } from './user/user-badge/user-badge.component';
import { ShowUserComponent } from './user/show-user/show-user.component';
import { MeComponent } from './user/me/me.component';
import { MatchComponent } from './tournaments/match/match.component';
import { TeamComponent } from './tournaments/team/team.component';
import { MatchBoxComponent } from './tournaments/match-box/match-box.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TournamentsListComponent } from './tournaments-list/tournaments-list.component';
import { PlayersReservationComponent } from './reservation/players-reservation/players-reservation.component';
import { SlotReservationButtonComponent } from './reservation/slot-reservation-button/slot-reservation-button.component';
import { CoursesListComponent } from './courses-list/courses-list.component';

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
    ForTPLoop,
    DatesComponent,
    CreateTeamListComponent,
    TournamentViewComponent,
    ShowTournamentComponent,
    CourseMainComponent,
    CourseDetailsComponent,
    CreateCourseComponent,
    // UserDetailsComponent,
    CoursesCompletedComponent,
    CoursesPendingComponent,
    LoginComponent,
    UserBadgeComponent,
    ShowUserComponent,
    MeComponent,
    MatchComponent,
    TeamComponent,
    MatchBoxComponent,
    DashboardComponent,
    TournamentsListComponent,
    MatchBoxComponent,
    PlayersReservationComponent,
    SlotReservationButtonComponent,
    CoursesListComponent,
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
    NgTournamentTreeModule,
    MatSelectModule,
    MatTabsModule,
    MatCardModule,
    MatDividerModule,
  ],
  //crea correttamente la data, ma mantiene anche le informazioni non utili (ad esempio l'ora)
  providers: [
    authInterceptorProviders,
    // { provide: HTTP_INTERCEPTORS, useClass: JsonInterceptor, multi: true },
    // { provide: JsonParser, useClass: CustomJsonParser },
    // { provide: Serialization, useClass: Serialization},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
