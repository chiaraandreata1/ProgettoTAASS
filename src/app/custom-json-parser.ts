import {Injectable} from "@angular/core";
import {JsonParser} from "./utilities/json-interceptor";
import {Serialization} from "./utilities/serialization";
import {UserService} from "./services/user.service";
import {UserInfo} from "./models/user-info";
import {Team} from "./models/team";
import {firstValueFrom, forkJoin, Observable} from "rxjs";
import {FacilityService} from "./services/facility.service";

export class JsonParsing {

  private observables: Observable<any>[];

  constructor(
    private userService: UserService,
    private facilityService: FacilityService,
    private body: string
  ) {
    this.observables = [];
  }

  /*public parse(): Observable<any> {
    let obj = JSON.parse(this.body, (key, value) => this.revive(key, value));

    forkJoin(this.observables)
  }*/

  private revive(key: string, value: any) {
    let ob;

    switch (key) {
      case 'date':
        value = Serialization.deserializeDate(value as string);
        break;
      case 'dates':
        value = (value as string[]).map(Serialization.deserializeDate);
        break;
      case 'players':
        ob = this.userService.getUsersInfo(value);
        ob.subscribe(players => value = players.map(player => player as UserInfo));
        this.observables.push(ob);
        break;
      case 'side0':
      case 'side1':
        if (value)
          value = value as Team;
        break;
      case 'sport':
        console.log(value);
        ob = this.facilityService.getSport(value);
        ob.subscribe(sport => value = sport);
        this.observables.push(ob);
        if (typeof value !== 'object') {
          let promise = firstValueFrom(this.facilityService.getSport(value));
          firstValueFrom(this.facilityService.getSport(value)).then(sport => {
            console.log(sport);
            value = sport;
          });
        }
        break;
    }
  }
}

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

    switch (key) {
      case 'endDateRegistration':
      case 'firstDayLesson':
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
        console.log(value);
        if (typeof value !== 'object') {
          let promise = firstValueFrom(this.facilityService.getSport(value));
          firstValueFrom(this.facilityService.getSport(value)).then(sport => {
            console.log(sport);
            value = sport;
          });
        }
        break;
    }

    return value;
  }

}
