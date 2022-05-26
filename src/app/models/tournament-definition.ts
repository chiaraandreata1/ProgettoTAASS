export class TournamentDefinition {

  constructor(
    public name: string,
    public sport: number,
    public price: number,
    public prize: number,
    public courtsCount: number,
    public maxTeamsNumber: number,
    public dates: string[],
    public description?: string,
  ) {
  }
}
