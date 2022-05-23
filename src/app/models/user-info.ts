export enum UserInfoType {
  PLAYER,
  TEACHER,
  ADMIN,
}

export class UserInfo {

  constructor(
    public id: number,
    public email: string,
    public displayName: string,
    public type: UserInfoType,
    public picture: string
  ) {
  }

  public toJSON(): string {
    return this.displayName;
  }

  public static toJSON(user: UserInfo): number {
    return user.id;
  }
}
