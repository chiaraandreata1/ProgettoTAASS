import {UserInfo} from "../models/user-info";
import {UserService} from "../user/user.service";
import {Team} from "../models/tournament";

export class TeamDisplay {

  constructor(
    team: Team,
    public players: (UserInfo | undefined)[],
    userService: UserService
  ) {
    players = []
  }

}

export class DisplayMatch {

  constructor(
    public id: number,

  ) {
  }
}

export class DisplayRound {

}
