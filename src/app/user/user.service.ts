import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable, of, ReplaySubject, tap} from 'rxjs';
import {UserInfo, UserInfoType} from "../models/user-info";
import {TokenStorageService} from "../services/token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = "http://localhost:8080/api/v1/user";

  private currentObs: ReplaySubject<UserInfo | undefined>;
  private currentUser?: UserInfo;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {
    this.currentObs = new ReplaySubject(1);

    if (tokenStorage.getToken()) {
      console.log("found");
      this.currentUser = tokenStorage.getUser();
      this.currentObs.next(this.currentUser);
    }
  }

  public getUser(userID: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/${userID}`);
  }

  public me(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/me`)
      .pipe(tap(me => this.currentUser = me)).pipe(tap(me => this.currentObs.next(me)));
  }

  public logout(): Observable<any> {
    return this.http.get(`${this.baseUrl}/log-out`).pipe(tap(() => this.currentObs.next(undefined)));
  }

  public isAdmin(): Observable<boolean> {
    return this.currentObs.pipe(map(u => u != undefined && u.type == "ADMIN"));
  }

  public isTeacher(): Observable<boolean> {
    return this.currentObs.pipe(map(u => u != undefined && u.type == "TEACHER"));
  }

  public isManager(): Observable<boolean> {
    console.log("manager");
    return this.currentObs.pipe(tap(console.log), map(u => {
      console.log("A");
      return u != undefined &&
      (u.type == "ADMIN" || u.type == "TEACHER")
    }));
  }

  public getCurrentUser(): UserInfo | undefined {
    return this.currentUser;
  }

  public currentUserObserver(): Observable<UserInfo | undefined> {
    return this.currentObs;
  }
}
