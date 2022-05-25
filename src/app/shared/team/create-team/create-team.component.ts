import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {UserInfo} from "../../../models/user-info";
import {OldUserService} from "../../../services/user.service";
import {Team} from "../../../models/team";

@Component({
  selector: 'app-create-team',
  templateUrl: './create-team.component.html',
  styleUrls: ['./create-team.component.css']
})
export class CreateTeamComponent implements OnInit, OnChanges {

  @Input() playersCount!: number;
  @Input() selectedPlayers!: UserInfo[];
  @Input() team?: Team;

  @Output() created: EventEmitter<Team> = new EventEmitter<Team>();
  @Output() updated: EventEmitter<Team> = new EventEmitter<Team>();

  public players!: (string | UserInfo)[];
  public touched!: boolean[];
  public suggestions: string[] = [];

  constructor(
    private userService: OldUserService,
    private elementRef: ElementRef
  ) {

  }

  private init(team?: Team) {
    this.players = team ? team.players : [];
    this.touched = [];
    for (let i = 0; i < this.playersCount; i++) {
      if (!team) this.players.push("");
      this.touched.push(false);
    }
    const inputs = this.elementRef.nativeElement.querySelectorAll("input");
    for (let i = 0; i < inputs.length; i++) {
      const player = this.players[i];
      inputs[i].value = player instanceof UserInfo ? player.displayName : player;
    }
  }

  ngOnInit(): void {
    this.init(this.team)
  }

  private updateValue(i: number, value: any) {
    if (typeof value === 'string') {
      this.players[i] = value;
    } else if (typeof value == 'object') {
      this.players[i] = value;
    } else {
      return;
    }
    this.updateSuggestions(i);
  }

  private selectedIDs(): number[] {
    let res = this.players.filter(p => typeof p === 'object').map(p => (p as UserInfo).id);
    res.push(...this.selectedPlayers.map(p => (p as UserInfo).id));
    return res;
  }

  private updateSuggestions(i: number) {
    const value = this.players[i];
    if (typeof value === 'object')
      this.suggestions = [];
    else {
      /*let users = */
      this.userService.suggestedUsers(value)
        .subscribe(
          value1 => {
            const users = value1.filter(user => !this.selectedIDs().includes(user.id));
            if (users.length == 1 && users[0].displayName.toLowerCase() === value.toLowerCase())
              this.updateValue(i, users[0]);
            else
              this.suggestions = users.map(value => value.displayName);
          }
        );
    }
  }

  suggestionSelected(suggestion: string, i: number) {
    this.userService.findUserInfo(suggestion).subscribe (
      value => {
        if (value)
          this.updateValue(i, value);
        else
          this.updateValue(i, suggestion);
      }
    )
  }

  valid(i: number): boolean {
    return !this.touched[i] || typeof this.players[i] === 'object';
  }

  onInput(i: number, $event: Event) {
    const el = ($event.target as HTMLInputElement);
    this.updateValue(i, el.value);
    el.value = typeof this.players[i] === 'object' ? (this.players[i] as UserInfo).displayName : this.players[i] as string;
    // this.update(el.value, i, el);
    // el.value = this.players[i] instanceof UserB ? (this.players[i] as UserB).username : this.players[i] as string;
  }

  enableSubmit(): boolean {
    return this.players.filter(value => !(typeof value === 'object')).length == 0;
  }

  onAdd() {
    if (this.enableSubmit())
      if (this.team) {
        this.team.players = this.players.map(value => value as UserInfo);
        this.updated.emit(this.team);
      } else {
        this.created.emit(new Team(this.players.map(value => value as UserInfo)));
      }
    this.team = undefined;
    this.init();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.init(this.team);
  }

  onFocusIn(i: number, event: FocusEvent) {
    const el = (event.target as HTMLInputElement);
    this.updateSuggestions(i);
  }
}
