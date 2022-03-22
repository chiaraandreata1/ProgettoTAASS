import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Sport} from "../../../models/sport";

@Component({
  selector: 'app-sport-selector',
  templateUrl: './sport-selector.component.html',
  styleUrls: ['./sport-selector.component.css']
})
export class SportSelectorComponent implements OnInit {

  public innerSelection?: Sport;
  public fromAbove?: Sport;

  @Input() sports!: Sport[];
  @Input() level!: number;
  @Input() set selected(sport: Sport | undefined) {
    this.fromAbove = sport;
    if (sport && this.sports.includes(sport))
      this.innerSelection = sport;
  }

  @Output() selectedChange: EventEmitter<Sport | undefined>;

  constructor() {
    if (!this.level)
      this.level = 0;
    this.selectedChange = new EventEmitter<Sport | undefined>();
  }

  ngOnInit(): void {
  }

  onSelect(sport?: Sport): void {
    console.log(sport);
    this.innerSelection = sport;
    if (!sport || !sport.children)
      this.selectedChange.emit(sport);
    else
      this.selectedChange.emit(undefined);
  }


}
