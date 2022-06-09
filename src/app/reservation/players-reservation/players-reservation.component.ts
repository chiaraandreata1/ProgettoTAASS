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
import {UserInfo} from "../../models/user-info";
import {Team} from "../../models/tournament";
import {debounceTime, distinctUntilChanged, Subject, switchMap} from "rxjs";
import {UserService} from "../../user/user.service";
import {MatOptionSelectionChange} from "@angular/material/core";
@Component({
  selector: 'app-players-reservation',
  templateUrl: './players-reservation.component.html',
  styleUrls: ['./players-reservation.component.css']
})
export class PlayersReservationComponent implements OnInit, OnChanges  {

  @Input() playersCount!: number;
  @Input() selectedPlayers: number[] = [];
  team?: Team;

  @Output() created: EventEmitter<Team> = new EventEmitter<Team>();
  @Output() updated: EventEmitter<Team> = new EventEmitter<Team>();

  public players!: (string | UserInfo)[];
  public touched!: boolean[];
  public suggestions: string[] = [];

  public users!: (UserInfo | undefined)[];

  public candidates: UserInfo[] = [];
  private partial = new Subject<string>();

  constructor(
    private userService: UserService,
    private elementRef: ElementRef
  ) {}

  private init(team?: Team) {
    // this.players = team ? team.players : [];
    this.users =
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

  private _init() {
    if (this.team)
      this.userService.getUsers(this.team.players)
        .subscribe(players => this.users = players);
    else
      this.users = new Array<UserInfo>(this.playersCount);

    this.touched = new Array<boolean>(this.playersCount).fill(false);
  }

  ngOnInit(): void {
    this._init();
    // this.init(this.team)

    const findPlayers = this.userService.findPlayers.bind(this.userService);

    this.partial.pipe(

      debounceTime(500),

      distinctUntilChanged(),

      switchMap(p => findPlayers(p, 5, this.selectedIDs()))

    ).subscribe(
      candidates => {
        // console.log(candidates)
        this.candidates =   candidates
      }
    );
  }

  ngOnChanges(changes: SimpleChanges): void {

    // this._init();
    // this.init(this.team);
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
    let res = this.users.filter(p => p != undefined).map(p => (p as UserInfo).id);
    res.push(...this.selectedPlayers);
    console.log(res);
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

  selected(index: number, user: UserInfo, event: MatOptionSelectionChange<UserInfo>) {
    this.users[index] = user;
    (event.source._getHostElement() as HTMLInputElement).value = user?.displayName;
    this.sendOutput();
    // console.log((event.source._getHostElement() as HTMLInputElement).value);
  }


  valid(i: number): boolean {
    return !this.touched[i] || this.users[i] != undefined;
  }



  onInput(i: number, event: Event) {

    if (event.target instanceof HTMLInputElement)
      this.partial.next((event.target as HTMLInputElement).value);

  }

  enableSubmit(): boolean {
    return this.users.filter(value => value == undefined).length == 0;
  }

  sendOutput() {
    this.created.emit(new Team(this.users.map(u => u?.id as number)));
    // this.team = undefined;
    // this.init();
  }

  onFocusIn(i: number, target: EventTarget | null) {
    this.candidates = [];

    if (target instanceof HTMLInputElement)
      this.partial.next((target as HTMLInputElement).value);
  }

  onFocusOut(index: number) {

    if (this.candidates.length == 1)
      this.users[index] = this.candidates[0];

    this.touched[index] = true;
  }

  deselect(index: number) {
    this.users[index] = undefined;
    this.sendOutput()
  }
}