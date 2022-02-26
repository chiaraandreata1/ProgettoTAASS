import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReservationsListComponent } from './reservations-list/reservations-list.component';
import { CreateReservationComponent } from "./create-reservation/create-reservation.component";
import {BoardsListComponent} from "./boards-list/boards-list.component";
import {CreateBoardComponent} from "./create-board/create-board.component";


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
