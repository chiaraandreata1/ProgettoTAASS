import {Component, OnInit} from '@angular/core';
import {Tournament} from "../models/tournament";
import {TournamentsService} from "../tournaments/tournaments.service";
import {UserService} from "../user/user.service";
import {FacilityHours} from "../models/facilityHours";
import {FacilityService} from "../services/facility.service";

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

    this.tournamentService.upcomingTournaments()
      .subscribe(value => {
        this.upcoming = value;
      })

    this.user.currentUserObserver().subscribe(
      user => {
        if (user) {
          this.tournamentService.myTournaments()
            .subscribe(value => this.myTournaments = value);
        }
      }
    )
  }

}
