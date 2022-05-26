import {Component, Input, OnInit} from '@angular/core';
import {Match} from "../../models/tournament";

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.css']
})
export class MatchComponent implements OnInit {

  @Input() public match?: Match

  constructor() { }

  ngOnInit(): void {
  }

}
