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
import {UserB} from "../../../models/user-b";
import {UserService} from "../../../services/user.service";
import {Team} from "../../../models/team";

@Component({
  selector: 'app-create-team',
  templateUrl: './create-team.component.html',
  styleUrls: ['./create-team.component.css']
})
export class CreateTeamComponent implements OnInit, OnChanges {

  @Input() playersCount!: number;
  @Input() selectedPlayers!: UserB[];
  @Input() team?: Team;

  @Output() created: EventEmitter<Team> = new EventEmitter<Team>();
  @Output() updated: EventEmitter<Team> = new EventEmitter<Team>();

  public players!: (string | UserB)[];
  public touched!: boolean[];
  public suggestions: string[] = [];

  constructor(
    private userService: UserService,
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
    for (let i = 0; i < inputs.length ; i++) {
      const player = this.players[i];
      inputs[i].value = player instanceof UserB ? player.username : player;
    }
  }

  ngOnInit(): void {
    this.init(this.team)
  }

  private updateValue(i: number, value: any) {
    if (typeof value === 'string') {
      this.players[i] = value;
    } else if (value instanceof UserB) {
      this.players[i] = value;
    } else {
      return;
    }
    this.updateSuggestions(i);
  }

  private updateSuggestions(i: number) {
    const value = this.players[i];
    if (value instanceof UserB)
      this.suggestions = [];
    else {
      let users = this.userService.suggestedUsers(value)
        .filter(value => !this.selectedPlayers.includes(value))
        .filter(value => !this.players.includes(value) || this.players.indexOf(value) == i);
      if (users.length == 1 && users[0].username.toLowerCase() === value.toLowerCase())
        this.updateValue(i, users[0]);
      else
        this.suggestions = users
          .map(value => value.username);
    }
  }

  suggestionSelected(suggestion: string, i: number) {
    const player = this.userService.getUserB(suggestion);
    if (player)
      this.updateValue(i, player)
    else
      this.updateValue(i, suggestion)
  }

  valid(i: number): boolean {
    return !this.touched[i] || this.players[i] instanceof UserB;
  }

  onInput(i: number, $event: Event) {
    const el = ($event.target as HTMLInputElement);
    this.updateValue(i, el.value);
    el.value = this.players[i] instanceof UserB ? (this.players[i] as UserB).username : this.players[i] as string;
    // this.update(el.value, i, el);
    // el.value = this.players[i] instanceof UserB ? (this.players[i] as UserB).username : this.players[i] as string;
  }

  enableSubmit(): boolean {
    return this.players.filter(value => !(value instanceof UserB)).length == 0;
  }

  onAdd() {
    if (this.enableSubmit())
      if (this.team) {
        this.team.players = this.players.map(value => value as UserB);
        this.updated.emit(this.team);
      } else {
        this.created.emit(new Team(this.players.map(value => value as UserB)));
      }
    this.team = undefined;
    this.init();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.init(this.team);
  }
}
