import {Component, Input, OnInit} from '@angular/core';
import {Match, Team, Tournament} from "../../models/tournament";
import {Serialization} from "../../utilities/serialization";
import {UserService} from "../../user/user.service";
import {FacilityService} from "../../services/facility.service";
import {UserInfo} from "../../models/user-info";
import {Observable, tap} from "rxjs";
import {Sport} from "../../models/sport";
import {TournamentsService} from "../tournaments.service";

@Component({
  selector: 'app-tournament-view',
  templateUrl: './tournament-view.component.html',
  styleUrls: ['./tournament-view.component.css']
})
export class TournamentViewComponent implements OnInit {

  @Input() public tournament!: Tournament;

  public owner?: UserInfo;
  public sport?: Sport;

  // public owner$!: Observable<UserInfo>;
  // public sport$!: Observable<Sport>;

  public overMatch?: Match;
  public selected?: Match;
  public waiting: boolean;
  public error: any;
  public create_team: boolean = false;

  constructor(
    public userService: UserService,
    public facilityService: FacilityService,
    public tournamentService: TournamentsService
  ) {
    this.waiting = false;
  }

  ngOnInit(): void {
    this.userService.getUser(this.tournament.owner).subscribe(u => {
      this.owner = u;
    })
    this.facilityService.getSport(this.tournament.sport).subscribe(s => this.sport = s);
  }

  public alreadySelected(): number[] {
    return this.tournament.teams.flatMap(value => value.players);
  }

  winner(top: boolean, match: Match) {
    let res = match.points0 + match.points1 > 0 && match.points0 > match.points1 == top;
    // if (res) res = match.points0 > match.points1 == top;
    return res;
  }

  teamLabel(team: Team | undefined) {
    let res;
    if (team) {
      // res = team.players[0].displayName;
      // for (let i = 1; i < team.players.length; i++) res += ` - ${team.players[i].displayName}`
    }
    return res;
  }

  formatDate(match: Match) {
    return match.date;
  }

  champion() {
    let rounds = this.tournament.rounds;
    let final = rounds[rounds.length - 1].matches[0];
    if (final.points0 + final.points1 > 0)
      return final.points1 > final.points0 ? final.side1 : final.side0;
    return undefined;
  }

  confirm() {
    console.log("confirm")
    this.waiting = true;
    this.tournamentService.confirm(this.tournament.id).subscribe({
      next: tournament => {
        this.tournament = tournament;
        this.waiting = false;
      },
      error: error => {
        this.error = error;
        this.waiting = false;
      }
    })
  }

  addTeam() {
    this.create_team = true;
  }

  join() {
    if (this.userService.getCurrentUser()) {
      let id = this.userService.getCurrentUser()?.id;
      if (id) {
        if (this.sport?.playersPerTeam == 1) {
          this.onTeamCreate(new Team([id]))
        } else {
          this.create_team = true;
        }
      }
    }
  }

  onTeamCreate(team: Team) {
    let type = this.userService.getCurrentUser()?.type;

    if (type) {
      if (type == 'ADMIN' || type == 'TEACHER') {
        this.waiting = true;
        this.tournamentService.addPlayers(this.tournament.id, team.players)
          .subscribe({
            next: t => {
              this.tournament = t;
              this.create_team = false;
              this.waiting = false;
            },
            error: msg => {
              this.error = msg.error.message;
              this.waiting = false;
            }
          })
      }
      if (type == 'PLAYER') {
        this.waiting = true;
        this.tournamentService.join(this.tournament.id, team.players)
          .subscribe({
            next: t => {
              this.tournament = t;
              this.create_team = false;
              this.waiting = false;
            },
            error: msg => {
              this.error = msg.error.message;
              this.waiting = false;
            }
          })
      }
    }
  }

  cancel() {
    this.waiting = true;
    this.tournamentService.cancel(this.tournament.id)
      .subscribe({
        next: t => {
          this.tournament = t;
          this.waiting = false;
        },
        error: e => {
          this.error = e.error.message;
          this.waiting = false;
        }
      })
  }

  complete() {
    this.waiting = true
    this.tournamentService.complete(this.tournament.id)
      .subscribe({
        next: t => {
          this.tournament = t;
          this.waiting = false;
        },
        error: e => {
          this.error = e.error.message;
          this.waiting = false;
        }
      });
  }

  onMatchUpdate(match: Match) {
    this.waiting = true;
    this.tournamentService.matchResults(this.tournament.id, match)
      .subscribe({
        next: t => {
          this.tournament = t;
          this.waiting = false;
          this.selected = undefined;
        },
        error:e => {
          this.error = e.error.message;
          this.waiting = false;
        }
      });
  }

  showJoin(user?: UserInfo): boolean {
    if (!user || user.type != 'PLAYER')
      return false;

    let show = true;
    let teams = this.tournament.teams;

    for (const team of teams)
      show = show && !team.players.includes(user?.id);

    if (show)
      show = this.tournament.status == 'CONFIRMED';

    return show;
  }

  isActiveTeam(team: Team): boolean {
    let user = this.userService.getCurrentUser();
    if (user && user.id) {
      return team.players.includes(user.id);
    }
    return false;
  }
}
