import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Sport} from "../../models/sport";
import {UserInfo} from "../../models/user-info";
import {TournamentsService} from "../tournaments.service";
import {Tournament} from "../../models/tournament";
import {Router} from "@angular/router";
import {FacilityService} from "../../services/facility.service";
import {TournamentDefinition} from "../../models/tournament-definition";
import {UserService} from "../../user/user.service";
import {Serialization} from "../../utilities/serialization";

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

  tournamentBuilding!: TournamentDefinition;
  // teams: Team[] = [];

  selectedPlayers!: UserInfo[];
  // editing?: Team;
  waiting: boolean = false;
  // level?: string;
  error?: string;

  constructor(
    private router: Router,
    private facilityService: FacilityService,
    private tournamentService: TournamentsService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    // this.teams = [
    //   // new Team([new UserB("Alfio")])
    // ];

    let user = this.userService.getCurrentUser();

    if (!user || user.type == "PLAYER")
      this.router.navigate(["login"]);

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

    this.tournamentBuilding = new TournamentDefinition(
      // @ts-ignore
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined
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
    this.tournamentBuilding.dates = e.dates.map((value: Date) => Serialization.serializeDate(value, false));
  }

  selectedSport(sport?: Sport) {
    this.sport = sport;
    if (sport)
      this.facilityService.getCourtsForSport(sport).subscribe(courts => this.courtNumber = courts.length);
    else
      this.courtNumber = undefined;
    // this.tournamentBuilding.sport = sport;
  }

  // createdTeam(team: Team) {
  //   this.teams.push(team);
  //   for (const player of team.players)
  //     this.selectedPlayers.push(player);
  // }
  //
  // updatedTeam(team: Team) {
  //   for (const player of team.players)
  //     this.selectedPlayers.push(player);
  //   this.editing = undefined;
  // }
  //
  // deleteTeam(team: Team) {
  //   this.teams = this.teams.filter(value => value !== team);
  //   this.selectedPlayers = this.selectedPlayers.filter(value => !team.players.includes(value))
  // }
  //
  // updateTeam(team: Team) {
  //   console.log("updated", team);
  //   this.selectedPlayers = this.selectedPlayers.filter(value => !team.players.includes(value))
  //   this.editing = team;
  // }

  externalValid() {
    return this.sport;
  }

  submit() {
    if (this.sport) {
      // this.waiting = true;
      // this.tournamentBuilding.teams = this.teams;
      this.tournamentBuilding.sport = this.sport?.id;

      this.tournamentService.create(this.tournamentBuilding).subscribe({
        next: tournament => this.router.navigate(['/', 'tournament', tournament.id]),
        error: msg => this.error = msg
      });

      // this.tournamentService.createTournament(this.tournamentBuilding).subscribe(value => {
      //   console.log(value);
      //   this.tournament = value;
      //   this.waiting = false;
      // });
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
      this.tournamentService.confirm(this.tournament.id).subscribe(value => this.router.navigate(['tournaments', 'info'], {queryParams: {'id': value.id}}));
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
