import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Sport} from "../../models/sport";
import {TournamentBuilding} from "../../models/tournament-building";
import {Team} from "../../models/team";
import {UserInfo} from "../../models/user-info";
import {TournamentsService} from "../tournaments.service";
import {Match, Tournament, TournamentRound} from "../../models/tournament";
import {Router} from "@angular/router";
import {FacilityService} from "../../services/facility.service";

@Component({
  selector: 'app-create-tournaments',
  templateUrl: './create-tournament.component.html',
  styleUrls: ['./create-tournament.component.css', './bootstrap-datepicker.standalone.css']
})
export class CreateTournamentComponent implements OnInit, AfterViewInit {
  @ViewChild('teamsList') teamList!: HTMLInputElement;

  sports?: Sport[];
  sport?: Sport;
  courtNumber?: number;

  tournament?: Tournament;

  tournamentBuilding!: TournamentBuilding;
  teams: Team[] = [];

  selectedPlayers!: UserInfo[];
  editing?: Team;
  waiting: boolean = false;
  level?: string;

  constructor(
    private router: Router,
    private facilityService: FacilityService,
    private tournamentService: TournamentsService
  ) {
  }

  ngOnInit(): void {
    this.teams = [
      // new Team([new UserB("Alfio")])
    ];
    const boundUpdateDates = this.updateDates.bind(this);

    this.facilityService.getSports().subscribe(value => {
      this.sports = value;
      setTimeout(() => {
        // @ts-ignore
        $('#tournament-dates').datepicker({
          multidate: true,
          format: 'dd/mm/yyyy',
          clearBtn: true
        }).on(
          'changeDate', boundUpdateDates
        );
      }, 0);
    });

    this.tournamentBuilding = new TournamentBuilding(
      // @ts-ignore
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
    );

    this.selectedPlayers = [];
  }

  ngAfterViewInit() {
    const boundUpdateDates = this.updateDates.bind(this);

    // console.log($('#tournament-dates'));
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
    if (sport)
      this.facilityService.getCourtsForSport(sport).subscribe(courts => this.courtNumber = courts.length);
    else
      this.courtNumber = undefined;
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
        console.log(value);
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

  formatLevel(l: string): string {
    return l[0].toUpperCase() + l.substring(1).toLowerCase();
  }

  dateInit() {
    const boundUpdateDates = this.updateDates.bind(this);
    console.log("A");
    // @ts-ignore
    $('#tournament-dates').datepicker({
      multidate: true,
      format: 'dd/mm/yyyy',
      clearBtn: true
    }).on(
      'changeDate', boundUpdateDates
    );
  }
}