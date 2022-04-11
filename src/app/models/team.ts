import {UserInfo} from "./user-info";

export class Team {

  constructor(
    public players: UserInfo[]
  ) {
  }

  public static toJSON(team: Team): object {
    return {
      players: team.players.map(UserInfo.toJSON)
    }
  }
}
