import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReservationsListComponent } from './reservation/reservations-list/reservations-list.component';
import { CreateReservationComponent } from "./reservation/create-reservation/create-reservation.component";
import {BoardsListComponent} from "./board/boards-list/boards-list.component";
import {CreateBoardComponent} from "./board/create-board/create-board.component";
import {ReservationMainComponent} from "./reservation/reservation-main/reservation-main.component";
import {BoardMainComponent} from "./board/board-main/board-main.component";
import {BoardPersonalComponent} from "./board/board-personal/board-personal.component";


const routes: Routes = [
  { path: 'reservation-main', component: ReservationMainComponent,
    children: [{ path: 'reservation', component: ReservationsListComponent }, { path: 'addReservation', component: CreateReservationComponent }]},
  { path: 'board-main', component: BoardMainComponent,
    children: [{ path: 'board', component: BoardsListComponent },  { path: 'addBoard', component: CreateBoardComponent }, { path: 'yourBoards', component: BoardPersonalComponent }]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }