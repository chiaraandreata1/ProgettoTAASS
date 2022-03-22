export class Sport {

  constructor(
    public id: number,
    public name: string,
    public players: number,
    public children?: Sport[]
  ) {
  }
}
