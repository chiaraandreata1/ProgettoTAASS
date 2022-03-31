import {Injectable} from "@angular/core";
import {JsonParser} from "./utilities/json-interceptor";
import {Serialization} from "./utilities/serialization";
import {UserService} from "./services/user.service";
import {UserInfo} from "./models/user-info";
import {Team} from "./models/team";
import {firstValueFrom} from "rxjs";
import {FacilityService} from "./services/facility.service";

@Injectable()
export class CustomJsonParser implements JsonParser {

  constructor(
    private serialization: Serialization,
    private userService: UserService,
    private facilityService: FacilityService
  ) {
  }

  parse(text: string): any {
    console.log(text);
    return JSON.parse(text, (key, value) => this.revive(key, value, this.serialization));
  }

  private revive(key: string, value: any, serialization: Serialization) {
    // console.log(key, value)
    switch (key) {
      case 'date':
        value = Serialization.deserializeDate(value as string);
        break;
      case 'dates':
        value = (value as string[]).map(Serialization.deserializeDate);
        break;
      case 'players':
        firstValueFrom(this.userService.getUsersInfo(value)).then(players => {
          value = players.map(player => player as UserInfo);
        });
        break;
      case 'side0':
      case 'side1':
        if (value)
          value = value as Team;
        break;
      case 'sport':
        if (typeof value !== 'object')
          firstValueFrom(this.facilityService.getSport(value)).then(sport => value = sport);
        break;
    }
    // console.log(value)
    return value;
  }

}
