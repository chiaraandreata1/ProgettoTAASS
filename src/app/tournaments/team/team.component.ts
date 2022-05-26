import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {UserInfo} from "../../models/user-info";
import {UserService} from "../../user/user.service";
import {Team} from "../../models/tournament";

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit, OnChanges {

  @Input() public team?: Team;

  public players: UserInfo[] = [];
  public teamString: string = "";

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.update();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.update();
  }

  private update() {
    if (this.team)
      this.userService.getUsers(this.team?.players as unknown as number[])
        .subscribe(users => {
          this.players = users;
          this.teamString = users.map(v => v.displayName).join(" - ");
        });
    else {
      this.players = [];
      this.teamString = "";
    }
  }

}
