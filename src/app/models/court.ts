import {Sport} from "./sport";

export class Court {

  constructor(
    public id: number,
    public sport: Sport,
    public status: string
  ) {
  }
}
