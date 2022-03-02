import {Court} from "./court";

export class Reservation {
  id!: number;
  sportReservation!: string;
  dateReservation!: string;
  hourReservation!: number;
  courtReservation!: Court;
}
