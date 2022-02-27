import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReservationsListComponent } from './reservation/reservations-list/reservations-list.component';
import { CreateReservationComponent } from "./reservation/create-reservation/create-reservation.component";
import {BoardsListComponent} from "./board/boards-list/boards-list.component";
import {CreateBoardComponent} from "./board/create-board/create-board.component";


const routes: Routes = [
  { path: '', redirectTo: 'reservation', pathMatch: 'full' },
  { path: 'reservation', component: ReservationsListComponent },
  { path: 'addReservation', component: CreateReservationComponent },
  { path: 'board', component: BoardsListComponent },
  { path: 'addBoard', component: CreateBoardComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
