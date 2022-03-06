import {Court} from "./court";

export class Reservation {
  id!: number;
  sportReservation!: string;
  dateReservation!: string;
  hourReservation!: number;
  courtReservation!: Court;
  player1!: string;
  player2!: string;
  player3!: string;
  player4!: string;
}
