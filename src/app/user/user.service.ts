import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable, of, ReplaySubject, tap} from 'rxjs';
import {UserInfo} from "../models/user-info";
import {TokenStorageService} from "../services/token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = "http://ball.net:8080/api/v1/user";

  private currentObs: ReplaySubject<UserInfo | undefined>;
  private currentUser?: UserInfo;

  public isManager$: Observable<boolean>;
  public isPlayer$: Observable<boolean>;
  public isAdmin$: Observable<boolean>;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {
    this.currentObs = new ReplaySubject(1);

    if (tokenStorage.getToken()) {
      // console.log("found");
      this.currentUser = tokenStorage.getUser();
      this.currentObs.next(this.currentUser);
    }

    this.isManager$ = this.currentObs.pipe(map(u => u != undefined && (u.type == 'ADMIN' || u.type == 'TEACHER')));

    this.isPlayer$ = this.currentObs.pipe(map(u => u != undefined && u.type == 'PLAYER'));

    this.isAdmin$ = this.currentObs.pipe(map(u => u != undefined && u.type == 'ADMIN'));
  }

  public getUser(userID: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/get`, {params: {id: userID}});
  }

  public me(): Observable<UserInfo> {
    // console.log("me");
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
    return this.currentObs.pipe(/*tap(console.log), */map(u => {
      console.log(u?.type)
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

  getUsers(userIDs: number[]): Observable<UserInfo[]> {
    return this.http.get<UserInfo[]>(`${this.baseUrl}/get-users`, {params: {ids: userIDs}});
  }

  public suggestedUsers(hint: string, limit: number = 5, excluded: number[] = []): Observable<UserInfo[]> {
    if (hint == "")
      return of([]);
    return this.http.get<UserInfo[]>(`${this.baseUrl}/find-users`, {params: {
        query: hint,
        excluded: excluded,
        limit: limit
      }}).pipe(
        tap(console.log)
    );
  }

  public findPlayers(hint: string, limit: number = 5, excluded: number[] = []): Observable<UserInfo[]> {
    if (hint == "")
      return of([]);
    return this.http.get<UserInfo[]>(`${this.baseUrl}/find-players`, {params: {
        query: hint,
        excluded: excluded,
        limit: limit
      }}).pipe(
      tap(console.log)
    );
  }

  public findInstructors(){
    return this.http.get<UserInfo[]>(`${this.baseUrl}/get-instructors`);
  }
}
