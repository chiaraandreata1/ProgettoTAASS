import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of, tap} from 'rxjs';
import {UserInfo} from "../models/user-info";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = "http://localhost:8080/api/v1/user";

  private currentUser?: any;

  constructor(
    private http: HttpClient
  ) {
  }

  public getCurrentUser(force = false): Observable<any | undefined> {

    let observable: Observable<any>;
    if (!force && this.currentUser)
      observable = of(this.currentUser);
    else
      observable = this.http.get(`${this.baseUrl}/me`, {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
      })
        .pipe(tap(console.log));
    return observable;
  }

  public test(): Observable<any | undefined> {
    console.log("Test inner");
    let observable = this.http.get("http://localhost:8080/api/v1/boards/test");
    return observable;
  }

  public a(): Observable<any | undefined> {
    console.log("TEST A");
    let obs = this.http.get("http://localhost:8080/a/test");
    return obs;
  }

  public getUser(userID: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/${userID}`);
  }

  public me(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/me`);
  }

  public logout(): Observable<any> {
    return this.http.get(`${this.baseUrl}/log-out`);
  }
}
