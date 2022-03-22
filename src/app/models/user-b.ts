export class UserB {

  constructor(
    public username: string
  ) {
  }

  public toJSON(): string {
    return this.username;
  }
}
