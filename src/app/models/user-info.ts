export enum UserInfoType {
  PLAYER,
  TEACHER,
  ADMIN,
}

export class UserInfo {

  constructor(
    public id: number,
    public userName: string,
    public shownName: string,
    public type: UserInfoType
  ) {
  }

  public toJSON(): string {
    return this.userName;
  }
}
