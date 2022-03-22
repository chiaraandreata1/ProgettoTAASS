import {Team} from "./team";
import {Sport} from "./sport";
import {Serialization} from "../utilities/serialization";

export class TournamentBuilding {

  constructor(
    public name: string,
    public sport: Sport, //Sport ID
    public price: number,
    public prize: number,
    public level: string,
    public courtCount: number,
    public description?: string,
    public teams: Team[] = [],
    public dates: Date[] = [],
  ) {
    this.teams = [];
  }

  public toJSON(): object {
    return {
      "name": this.name,
      "description": this.description,
      "sport": this.sport.id,
      "price": this.price,
      "prize": this.prize,
      "level": this.level,
      "teams": this.teams,
      "dates": this.dates.map(value => Serialization.serializeDate(value, false))
    }
  }

//   toJSON(): object {
//     // return '"name": '
//     // return `{"name": ${this.name}}`;
//     return {
//       "name": this.name,
//       "description": this.description,
//       "sport": this.sport,
//       "price": this.price,
//       "prize": this.prize,
//       "level": this.level,
//       "teams": this.teams,
//       "dates": this.dates.map(value => serializeDate(value, false))
//     }
// //     return `{
// // "name": "${this.name}",
// // ${serializeNullable(this.description, "description", JSON.stringify, ',')}
// // "sport": "${this.sport}",
// // "price": ${this.price},
// // "prize": ${this.prize},
// // "level": "${this.level},
// // "teams": ${serializeArray(this.teams, e => JSON.stringify(e.players))},
// // "dates": ${serializeArray(this.dates, serializeDate)}
// // }`;
//   }

}
