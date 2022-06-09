import { Component, OnInit } from '@angular/core';
import {Tournament} from "../../models/tournament";
import {TournamentsService} from "../tournaments.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";

@Component({
  selector: 'app-mine',
  templateUrl: './mine.component.html',
  styleUrls: ['./mine.component.css']
})
export class MineComponent implements OnInit {

  LIMIT = 10;

  page: number = 0;

  tournaments?: Tournament[];

  target!: string;


  constructor(
    private tournamentService: TournamentsService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {

    let map = this.route.snapshot.queryParamMap;
    this.page = map.has("page") ? Number(map.get("page")) : 0;

    let observable: Observable<Tournament[]>;

    if (this.route.toString().includes("mine")) {
      this.target = "mine";
      observable = this.tournamentService.myTournaments(this.page, this.LIMIT);
    } else if (this.route.toString().includes("upcoming")) {
      this.target = "upcoming";
      observable = this.tournamentService.upcomingTournaments(this.page, this.LIMIT);
    } else {
      this.router.navigate(["/"]);
      return;
    }

    observable.subscribe(v => this.tournaments = v);
  }

  private navigate(page: number) {
    this.router.navigateByUrl("/", {skipLocationChange: true})
      .then(() => this.router.navigate(["/", "tournaments", this.target], {queryParams: {page: page}}));
  }

  public next() {
    this.navigate(this.page + 1);
  }

  public previous() {
    this.navigate(this.page - 1);
  }

}
