import { Component, OnInit } from '@angular/core';
import {NgttRound, NgttTournament} from "ng-tournament-tree";
import {FacilityService} from "../../services/facility.service";

@Component({
  selector: 'app-tournaments-main',
  templateUrl: './tournaments-main.component.html',
  styleUrls: ['./tournaments-main.component.css']
})
export class TournamentsMainComponent implements OnInit {

  selected?: Match;
  overMatch?: Match;

  rounds: Round[] = [
    new Round([
      new Match("07/03/22 15:00", "A", "F", 1, 0),
      new Match("07/03/22 15:00", "G", "C", 9, 11),
      new Match("07/03/22 17:00", "D", "H", 10, 5),
      new Match("07/03/22 17:00", "I", "B", 7, 13),
      new Match("07/03/22 19:00", "A", "F", 1, 0),
      new Match("07/03/22 19:00", "G", "C", 9, 11),
      new Match("07/03/22 21:00", "D", "H", 10, 5),
      new Match("07/03/22 21:00", "I", "B", 7, 13)
    ]),
    new Round([
      new Match("08/03/22 15:00", "A", "F", 1, 0),
      new Match("08/03/22 15:00", "G", "C", 9, 11),
      new Match("08/03/22 17:00", "D", "H", 10, 5),
      new Match("08/03/22 17:00", "I", "B", 7, 13)
    ]),
    new Round([
      new Match("09/03/22 17:00", "A", "C", ),
      new Match("09/03/22 17:00", "D", "B", 7, 13)
    ]),
    new Round([
      new Match("10/03/22 15:00"),
      // new Match("A", "B", 7, 5)
    ])
  ];

  constructor(
    private facilityService: FacilityService
  ) { }

  ngOnInit(): void {
    this.facilityService.getSports().subscribe(console.log);
    this.facilityService.getCourts().subscribe(console.log);
  }

  public winner(side: boolean, match: Match): boolean {
    if (match.side1 && match.side2) {
      if (match.points1 == match.points2)
        return true;

      return side == (match.side1 < match.side2);
    } else
      return false;
  }

}

class Match {

  date: string;
  side1?: String;
  side2?: String;
  points1?: number;
  points2?: number;

  constructor(date: string, side1?: String, side2?: String, points1?: number, points2?: number) {
    this.date = date;
    this.side1 = side1;
    this.side2 = side2;
    this.points1 = points1;
    this.points2 = points2;
  }
}

class Round {

  match: Match[];

  constructor(matched: Match[]) {
    this.match = matched;
  }
}
