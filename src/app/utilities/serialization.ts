import {Team} from "../models/team";
import {UserService} from "../services/user.service";
import {Injectable} from "@angular/core";

@Injectable()
export class Serialization {

  private getUsers;

  constructor(
    private userService: UserService
  ) {
    this.getUsers = userService.getUsersInfo.bind(this.userService);
  }

  public static serializeDate(date: Date, keepHours: boolean = false): string {
    let d = date.getDate();
    let m = date.getMonth() + 1;
    let y = date.getFullYear();
    return `${d < 10 ? '0' : ''}${d}/${m < 10 ? '0' : ''}/${y}`;
  }

  public static serializeDateTime(date: Date): string {
    let d = date.getDate();
    let M = date.getMonth() + 1;
    let y = date.getFullYear();
    const h = date.getHours();
    const m = date.getMinutes();

    return `${d < 10 ? '0' : ''}${d}/${M < 10 ? '0' : ''}/${y} ${h < 10 ? '0' : ''}${h}:${m < 10 ? '0' : ''}${m}`;
  }

  public static deserializeDate(string: String): Date {
    console.log(string);
    const date = new Date();
    const h = string.split(" ");
    const h0 = h[0].split("/").map(value => parseInt(value));
    date.setUTCFullYear(h0[2], h0[1], h0[0]);
    if (h.length > 1) {
      const h1 = h[1].split(":").map(value => parseInt(value));
      date.setUTCHours(h1[0] - 1, h1[1], 0);
    }
    return date;
  }

  public static serializeArray<T>(array: T[], serializer: (e: T) => string): string {
    let res = `[`;
    for (let i = 0; i < array.length;) {
      if (i > 0)
        res += ', '
      res += `"[${serializer(array[i])}]"`
    }
    return res;
  }

  public static serializeNullable<T>(t: T | undefined | null,
                                       key: string,
                                       serializer: (e: T) => string = JSON.stringify,
                                       endwith: string = ''): string {
    let res;
    if (t)
      res = `"${key}": ${serializer(t)}${endwith}`
    else
      res = ""
    return res;
  }

  // public deserializeTeamLambda() {
  //   const bGetUsers = this.userService.getUsers.bind(this.userService);
  //   return (players: string[]) => new Team(bGetUsers(players));
  // }
  //
  // public deserializeTeam(players: string[]): Team {
  //   return new Team(this.userService.getUsers(players))
  // }
}
