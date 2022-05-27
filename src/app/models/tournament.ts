import {UserInfo} from "./user-info";

export class Team {

  constructor(
    public players: number[]
  ) {
  }

  public static toJSON(team: Team): object {
    return {
      // players: team.players.map(UserInfo.toJSON)
    }
  }
}

export class Match {

  private _points0?: number[];
  private _points1?: number[];
  private _winner?: number;

  constructor(
    public id: number,
    public round: number,
    public roundHeight: number,
    public court: string,
    public date: string,
    public side0: Team,
    public side1: Team,
    public points0: number,
    public points1: number,
    public status: string
  ) {
  }

  static toJSON(match: Match): object {
    return {
      'id': match.id,
      'round': match.round,
      'roundHeight': match.roundHeight,
      'court': match.court,
      // 'date': Serialization.serializeDateTime(match.date),
      'side0': match.side0,
      'side1': match.side1,
      'points0': match.points0,
      'points1': match.points1,
    }
  }

  private static explodePoints(match: Match) {
    if (match.status == 'DONE') {
      if (match.points0 == null)
        console.error("A")
      let p0 = match.points0.toString().split("").map(Number);
      let p1 = match.points1.toString().split("").map(Number);

      const l = Math.max(p0.length, p1.length)
      if (l == 2)
        console.log(l);

      match._winner = Array.from(Array(l), (_, i) => {
        let x : number;

        if (p0.length <= i) {
          x = !p1[i] ? 0 : -1;
          p0.push(0);
        } else if (p1.length <= i) {
          x = 1;
          p1.push(0);
        } else if (p0[i] > p1[i]) {
          x = 1;
        } else if (p1[i] > p0[i]) {
          x = -1;
        } else
          x = 0;

        return x;
      })
        .reduce((a, b) => a + b)

      match._points0 = p0;
      match._points1 = p1;

      if (match._winner)
        match._winner /= Math.abs(match._winner);
    }
  }

  public static explodePoints0(match: Match): number[] {
    if (!match._points0) {
      this.explodePoints(match);
    }
    return match._points0 as number[];
  }

  public static explodePoints1(match: Match): number[] {
    if (!match._points1) {
      this.explodePoints(match);
    }
    return match._points1 as number[];
  }

  public static winner(match: Match): number {
    if (!match._winner) {
      this.explodePoints(match);
    }

    return match._winner as number;
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
    public status: string,
    public owner: number,
    public name: string,
    public sport: number,
    public price: number,
    public prize: number,
    public rounds: TournamentRound[],
    public teams: Team[],
    public maxTeamsNumber: number,
    public description?: string,
  ) {
  }

  static toJSON(tournament: Tournament) {
    return {
      // 'id': tournament.id,
      // 'name': tournament.name,
      // 'sport': tournament.sport.id,
      // 'price': tournament.price,
      // 'prize': tournament.prize,
      // 'level': tournament.level,
      // 'rounds': tournament.rounds.map(TournamentRound.toJSON),
      // 'description': tournament.description
    }
  }
}
