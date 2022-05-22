import {DummyCourt} from "./dummyCourt";
import {Serialization} from "../utilities/serialization";

export class Reservation {
  constructor(
    public id: number,
    public sportReservation: string,
    public date: Date,
    public typeReservation: string,
    public courtReservation: DummyCourt,
    public players: number[]
  ) {
  }

  static toJSON(reservation: Reservation): object {
    return {
      'id': reservation.id,
      'sportReservation': reservation.sportReservation,
      'date': Serialization.serializeDateTime(reservation.date),
      'courtReservation': reservation.courtReservation,
      'typeReservation': reservation.typeReservation,
      'players': reservation.players,
    }
  }
}
