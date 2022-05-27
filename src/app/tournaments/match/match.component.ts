import {Component, EventEmitter, HostBinding, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Match} from "../../models/tournament";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.css'],
  host: {'class': 'card my-3'}
})
export class MatchComponent implements OnInit, OnChanges {

  @Input() public match!: Match

  @Output() public onUpdate = new EventEmitter<Match>();

  @HostBinding('class.ready') isReady = false;
  @HostBinding('class.done') isDone = false;
  isWalkover = false;

  public points0!: number[];
  public points1!: number[];
  public top: boolean = false;
  public bottom: boolean = false;
  // public done!: boolean;
  setsCount: number = 3;

  constructor(
    public userService: UserService
  ) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.match.status == "READY") {

      this.isReady = true;
      this.isDone = false;

      if (this.match.side1) {
        this.points0 = Array(this.setsCount).fill(0);
        this.points1 = Array(this.setsCount).fill(0);
      } else {
        this.isWalkover = true;
      }

    } else if (this.match.status == "DONE") {

      this.isDone = true;
      this.isReady = false;
      this.points0 = Match.explodePoints0(this.match)
      this.points1 = Match.explodePoints1(this.match)

      if (Match.winner(this.match) > 0)
        this.top = true;
      else if (Match.winner(this.match) < 0)
        this.bottom = true;
    }

  }

  updateSets() {
    if (this.setsCount > 0) {
      let old = this.points0;

      this.points0 = Array.from(Array(this.setsCount), (_, i) => old[i] ? old[i] : 0);
      old = this.points1;
      this.points1 = Array.from(Array(this.setsCount), (_, i) => old[i] ? old[i] : 0);
    }

  }

  valid(): boolean {
    if (this.isWalkover)
      return true;

    const t = Math.ceil(this.setsCount / 2);

    let a = 0;
    let b = 0;

    for (let i = 0; i < t; i++) {
      if (this.points0[i] > this.points1[i] && this.points0[i] >= 6)
        a++;
      else if (this.points1[i] > this.points0[i] && this.points1[i] >= 6)
        b++;
    }

    return a == t || b == t;
  }

  emitMatch() {
    if (this.valid()) {
      if (this.isWalkover) {
        this.match.points0 = 66;
        this.match.points1 = 0;
      } else {
        this.match.points0 = Number(this.points0.join(""));
        this.match.points1 = Number(this.points1.join(""));
      }
      this.onUpdate.emit(this.match);
    }
  }
}
