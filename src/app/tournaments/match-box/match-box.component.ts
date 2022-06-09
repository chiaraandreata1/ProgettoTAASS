import {Component, EventEmitter, HostBinding, HostListener, Input, OnInit, Output} from '@angular/core';
import {Match} from "../../models/tournament";

@Component({
  selector: 'app-match-box',
  templateUrl: './match-box.component.html',
  styleUrls: ['./match-box.component.css']
})
export class MatchBoxComponent implements OnInit {

  @Input() match?: Match;

  @Output() selected = new EventEmitter<Match>();

  @HostBinding('class.ready') isReady = false;
  @HostBinding('class.done') isDone = false;

  public top: boolean = false;
  public bottom: boolean = false;

  constructor() {
  }

  ngOnInit(): void {

    if (this.match) {
      if (this.match.status == "READY")
        this.isReady = true;

      else if (this.match.status == "DONE") {

        this.isDone = true;
        if (Match.winner(this.match) > 0)
          this.top = true;
        else if (Match.winner(this.match) < 0)
          this.bottom = true;

      }
    }
  }

  @HostListener('click')
  onClick() {
    if (this.match && (this.isDone || this.isReady))
      this.selected.emit(this.match);
  }

  prettyStatus() {
    if (this.match?.status == 'WAITING')
      return "Scheduled";
    else
      return `${this.match?.status.charAt(0)}${this.match?.status.substring(1).toLocaleLowerCase()}`;
  }
}
