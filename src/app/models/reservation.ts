import {Serialization} from "../utilities/serialization";

export class Reservation {
  constructor(
    public id: number,
    public ownerID: number,
    public sportReservation: number,
    public date: string,
    public nHours: number,
    public typeReservation: string,
    public courtReserved: number,
    public players: number[]
  ) {
  }

  static toJSON(reservation: Reservation): object {
    return {
      'id': reservation.id,
      'ownerID': reservation.ownerID,
      'sportReservation': reservation.sportReservation,
      'date': reservation.date, //Serialization.serializeDateTime(reservation.date),
      'nHours': reservation.nHours,
      'courtReserved': reservation.courtReserved,
      'typeReservation': reservation.typeReservation,
      'players': reservation.players,
    }
  }
}
