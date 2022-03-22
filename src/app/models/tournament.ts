import {Team} from "./team";
import {Serialization} from "../utilities/serialization";

export class Match {

  constructor(
    public id: number,
    public round: number,
    public roundHeight: number,
    public court: string,
    public date: Date,
    public side0: Team,
    public side1: Team,
    public points0: number,
    public points1: number,
  ) {
  }

  static toJSON(match: Match):object {
    return {
      'id': match.id,
      'round': match.round,
      'roundHeight': match.roundHeight,
      'court': match.court,
      'date': Serialization.serializeDateTime(match.date),
      'side0': match.side0,
      'side1': match.side1,
      'points0': match.points0,
      'points1': match.points1,
    }
  }
}

export class TournamentRound {

  constructor(
    public matches: Match[]
  ) {
  }


  static toJSON(round: TournamentRound) {
    return {
      'matches': round.matches.map(Match.toJSON)
    }
  }
}

export class Tournament {

  constructor(
    public id: number,
    public name: string,
    public sport: string,
    public price: number,
    public prize: number,
    public level: string,
    public rounds: TournamentRound[],
    public description?: string,
  ) {
  }

  static toJSON(tournament: Tournament) {
    return {
      'id': tournament.id,
      'name': tournament.name,
      'sport': tournament.sport,
      'price': tournament.price,
      'prize': tournament.prize,
      'level': tournament.level,
      'rounds': tournament.rounds.map(TournamentRound.toJSON),
      'description': tournament.description
    }
  }
}
