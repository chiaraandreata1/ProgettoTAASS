import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Sport} from "../../models/sport";
import {TournamentBuilding} from "../../models/tournament-building";
import {Team} from "../../models/team";
import {UserInfo} from "../../models/user-info";
import {TournamentsService} from "../tournaments.service";
import {Match, Tournament, TournamentRound} from "../../models/tournament";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-tournaments',
  templateUrl: './create-tournament.component.html',
  styleUrls: ['./create-tournament.component.css', './bootstrap-datepicker.standalone.css']
})
export class CreateTournamentComponent implements OnInit, AfterViewInit {
  @ViewChild('teamsList') teamList!: HTMLInputElement;

  sports: Sport[] = [
    new Sport(1, "Padel", 2),
    new Sport(-1, "Tennis", 0, [
      new Sport(2, "Single", 1),
      new Sport(3, "Double", 2)
    ])
  ];

  sport?: Sport;

  tournament?: Tournament;

  tournamentBuilding!: TournamentBuilding;
  teams: Team[] = [];

  selectedPlayers!: UserInfo[];
  editing?: Team;
  waiting: boolean = false;

  constructor(
    private router: Router,
    private tournamentService: TournamentsService
  ) {
    // this.tournament = new Tournament(
    //   -1,
    //   "Un torneo",
    //   "Tennis singolo",
    //   0,
    //   0,
    //   "Pro",
    //   [
    //     new TournamentRound([
    //       new Match(
    //         -1,
    //         0,
    //         0,
    //         "1",
    //         new Date(),
    //         new Team([
    //           new UserB("Alfio")
    //         ]),
    //         new Team([
    //           new UserB("Betta")
    //         ]),
    //         6,
    //         0
    //       ),
    //       new Match(
    //         -1,
    //         0,
    //         1,
    //         "1",
    //         new Date(),
    //         new Team([
    //           new UserB("Claudio")
    //         ]),
    //         new Team([
    //           new UserB("Diana")
    //         ]),
    //         0,
    //         6
    //       )
    //     ]),
    //     new TournamentRound([
    //       new Match(
    //         -1,
    //         1,
    //         0,
    //         "1",
    //         new Date(),
    //         new Team([
    //           new UserB("Alfio")
    //         ]),
    //         new Team([
    //           new UserB("Diana")
    //         ]),
    //         4,
    //         6
    //       )
    //     ]),
    //   ],
    //   "Nel mezzo del cammin di nostra vita mi ritrovai per una selva oscura"
    // );
  }

  ngOnInit(): void {
    this.teams = [
      // new Team([new UserB("Alfio")])
    ];

    this.tournamentBuilding = new TournamentBuilding(
      // @ts-ignore
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
    );


    // this.tournamentBuilding.name = "Un torneo";
    // this.tournamentBuilding.courtCount = 1;
    // this.tournamentBuilding.price = 0;
    // this.tournamentBuilding.prize = 0;
    // this.tournamentBuilding.courtCount = 1;
    // this.tournamentBuilding.teams = this.teams;
    this.selectedPlayers = [];
    // @ts-ignore
    // this.sport = this.sports[1].children[0];

    // let dates = [
    //   Date()
    // ];
    // @ts-ignore
    // $('#tournament-dates').datepicker('setDates', dates);/
      // @ts-ignore
    // $('#tournament-dates').datepicker('show');
  }

  ngAfterViewInit() {
    const boundUpdateDates = this.updateDates.bind(this);

    // @ts-ignore
    $('#tournament-dates').datepicker({
      multidate: true,
      format: 'dd/mm/yyyy',
      clearBtn: true
    }).on(
      'changeDate', boundUpdateDates
    );
  }

  updateDates(e: any): void {
    this.tournamentBuilding.dates = e.dates;
  }

  selectedSport(sport?: Sport) {
    this.sport = sport;
    // this.tournamentBuilding.sport = sport;
  }

  createdTeam(team: Team) {
    this.teams.push(team);
    for (const player of team.players)
      this.selectedPlayers.push(player);
  }

  updatedTeam(team: Team) {
    for (const player of team.players)
      this.selectedPlayers.push(player);
    this.editing = undefined;
  }

  deleteTeam(team: Team) {
    this.teams = this.teams.filter(value => value !== team);
    this.selectedPlayers = this.selectedPlayers.filter(value => !team.players.includes(value))
  }

  updateTeam(team: Team) {
    console.log("updated", team);
    this.selectedPlayers = this.selectedPlayers.filter(value => !team.players.includes(value))
    this.editing = team;
  }

  externalValid() {
    return this.sport && this.teams && this.teams.length > 0 && this.tournamentBuilding.dates && this.tournamentBuilding.dates.length > 0;
  }

  submit() {
    if (this.externalValid()) {
      this.waiting = true;
      this.tournamentBuilding.teams = this.teams;
      this.tournamentBuilding.sport = this.sport as Sport;
      this.tournamentService.createTournament(this.tournamentBuilding).subscribe(value => {
        this.tournament = value;
        this.waiting = false;
      });
    }
  }

  dropDraft() {
    this.waiting = true;
    this.tournament = undefined;
    this.waiting = false;
  }

  confirmDraft() {
    this.waiting = true;
    if (this.tournament)
      this.tournamentService.confirm(this.tournament).subscribe(value => this.router.navigate(['tournaments', 'info'], {queryParams: {'id': value.id}}));
  }
}
