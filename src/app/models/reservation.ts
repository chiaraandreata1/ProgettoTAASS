import {DummyCourt} from "./dummyCourt";

export class Reservation {
  id!: number;
  sportReservation!: string;
  dateReservation!: string;
  hourReservation!: number;
  typeReservation!: string;
  courtReservation!: DummyCourt;
  players!: string[];
}
