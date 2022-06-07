import {Component, OnInit} from '@angular/core';
import {Tournament} from "../models/tournament";
import {TournamentsService} from "../tournaments/tournaments.service";
import {UserService} from "../user/user.service";
import {FacilityHours} from "../models/facilityHours";
import {FacilityService} from "../services/facility.service";

class Hours {
  public Opening!: number;
  public Closing!: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  days: any = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  hours?: FacilityHours;
  upcoming?: Tournament[];
  myTournaments?: Tournament[];

  constructor(
    private tournamentService: TournamentsService,
    private facility: FacilityService,
    public user: UserService
  ) {
  }

  public formatHours(): string {

    return `${String(this.hours?.Opening).padStart(2, '0')}:00 - ${String(this.hours?.Closing).padStart(2, '0')}:00`;
  }

  ngOnInit(): void {

    this.facility.getHours().subscribe(v => this.hours = v);

    // @ts-ignore
    this.upcoming = [
      new Tournament(1398, "CANCELLED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "DONE", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        8),
      new Tournament(1398, "COMPLETED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "CONFIRMED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "CONFIRMED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
    ]

    // @ts-ignore
    this.myTournaments = [
      new Tournament(1398, "CANCELLED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "DONE", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        8),
      new Tournament(1398, "COMPLETED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "CONFIRMED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
      new Tournament(1398, "CONFIRMED", 1, "D", 22, 5, 5, [],
        // @ts-ignore
        ['a', 'b'],
        18),
    ]

    // this.tournamentService.upcomingTournaments()
    //   .subscribe(value => this.tournaments = value);

    this.user.currentUserObserver().subscribe(
      // user => {
      //   if (user) {
      //     this.tournamentService.myTournaments()
      //       .subscribe(value => this.tournaments = value);
      //   }
      // }
    )
  }

}
