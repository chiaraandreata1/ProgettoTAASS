import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/user";
import {UserB} from "../models/user-b";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrlUsers = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) { }

  getUser(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrlUsers}/${id}`);
  }

  getUsersList(): Observable<any> {
    return this.http.get(`${this.baseUrlUsers}`);
  }

  getUsersByType(typeuser: string): Observable<any> {
    return this.http.get(`${this.baseUrlUsers}/typeuser/${typeuser}`)
  }

  private users: UserB[] = [
    new UserB("Alfio"),
    new UserB("Betta"),
    new UserB("Claudio"),
    new UserB("Diana"),
    new UserB("Elio"),
    new UserB("Federica"),
    new UserB("Gaio"),
    new UserB("Hensel"),
    new UserB("Imola"),
  ];

  public suggestedUsers(hint: string = "", limit = 5) {
    if (hint == "")
      return [];
    return this.users.filter(value => value.username.toLowerCase().includes(hint.toLowerCase())).slice(0, limit);
  }

  public getUserB(username: string): UserB {
    const res = this.users.filter(value => value.username.toLowerCase() === username.toLowerCase());
    if (res.length != 1)
      throw new Error("NOPE");
    return res[0];
  }

  public getUsers(usernames: string[]): UserB[] {
    return usernames.map(value => this.getUserB(value));
  }

}
