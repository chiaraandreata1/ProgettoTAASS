import {Component, Input, OnInit} from '@angular/core';
import {Tournament} from "../models/tournament";

@Component({
  selector: 'app-tournaments-list',
  templateUrl: './tournaments-list.component.html',
  styleUrls: ['./tournaments-list.component.css']
})
export class TournamentsListComponent implements OnInit {

  @Input() tournaments?: Tournament[];

  constructor() { }

  ngOnInit(): void {
  }

}
