import {Component, Input, OnInit} from '@angular/core';
import {Match, Tournament} from "../../models/tournament";
import {Team} from "../../models/team";
import {Serialization} from "../../utilities/serialization";

@Component({
  selector: 'app-tournament-view',
  templateUrl: './tournament-view.component.html',
  styleUrls: ['./tournament-view.component.css']
})
export class TournamentViewComponent implements OnInit {

  @Input() public tournament!: Tournament;
  public overMatch?: Match;
  public selected?: Match;

  constructor(
  ) { }

  ngOnInit(): void {
    console.log(this.tournament);
  }

  winner(top: boolean, match: Match) {
    let res = match.points0 + match.points1 > 0 && match.points0 > match.points1 == top;
    // if (res) res = match.points0 > match.points1 == top;
    return res;
  }

  teamLabel(team: Team | undefined) {
    let res;
    console.log(team)
    if (team) {
      res = team.players[0].username;
      for (let i = 1; i < team.players.length; i++) res += ` - ${team.players[i].username}`
    }
    return res;
  }

  formatDate(match: Match) {
    return Serialization.serializeDateTime(match.date);
  }

  champion() {
    let rounds = this.tournament.rounds;
    let final = rounds[rounds.length - 1].matches[0];
    if (final.points0 + final.points1 > 0)
      return final.points1 > final.points0 ? final.side1 : final.side0;
    return undefined;
  }
}
