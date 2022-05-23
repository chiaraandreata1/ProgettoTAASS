import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {UserInfo} from "../models/user-info";

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
    return this.http.get(`${this.baseUrlUsers}/allusers`);
  }

  getUsersByType(typeuser: string): Observable<any> {
    return this.http.get(`${this.baseUrlUsers}/typeuser/${typeuser}`)
  }

  public suggestedUsers(hint: string = "", limit = 5): Observable<UserInfo[]> {
    console.log(hint);
    if (hint == "")
      return of([]);
    return this.http.get<UserInfo[]>(`${this.baseUrlUsers}/suggestions`, {params: {
        input: hint,
        limit: limit
      }});
  }

  public findUserInfo(username: string): Observable<UserInfo | null> {
    return this.http.get<UserInfo | null>(`${this.baseUrlUsers}/find-user-info`, {params: {username: username}});
  }

  public getUserInfo(id: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrlUsers}/user-info`, {params: {id: id}});
  }

  public getUsersInfo(ids: number[]): Observable<UserInfo[]> {
    if (ids.length == 0)
      return of([]);
    return this.http.get<UserInfo[]>(`${this.baseUrlUsers}/user-info`, {params: {ids: ids}});
  }
}
