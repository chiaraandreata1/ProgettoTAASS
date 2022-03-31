export class Sport {

  constructor(
    public id: number,
    public name: string,
    public playersPerTeam: number,
    public levels: string[],
    public parent?: Sport,
    public children?: Sport[]
  ) {
  }
}
