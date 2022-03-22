import { Component, OnInit } from '@angular/core';
import {Tournament} from "../../models/tournament";
import {ActivatedRoute, Router} from "@angular/router";
import {TournamentsService} from "../tournaments.service";

@Component({
  selector: 'app-show-tournament',
  templateUrl: './show-tournament.component.html',
  styleUrls: ['./show-tournament.component.css']
})
export class ShowTournamentComponent implements OnInit {

  tournament?: Tournament;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tournamentService: TournamentsService
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.queryParamMap.get("id");
    if (idParam) {
      this.tournamentService.get(parseInt(idParam)).subscribe(
        tournament => {
          if (!tournament)
            this.router.navigate(["/"]);
          else
            this.tournament = tournament;
        }
      )
    } else {
      this.router.navigate(["/"]);
    }
  }

}
