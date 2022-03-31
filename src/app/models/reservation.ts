import {DummyCourt} from "./dummyCourt";

export class Reservation {
  id!: number;
  sportReservation!: string;
  dateReservation!: string;
  hourReservation!: number;
  courtReservation!: DummyCourt;
  players!: string[];
}
