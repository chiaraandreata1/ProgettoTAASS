import {Injectable} from '@angular/core';
import {ReplaySubject, Subject} from "rxjs";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  private currentUser: ReplaySubject<any> = new ReplaySubject<any>(1);

  constructor() {
    const _user = sessionStorage.getItem(USER_KEY)
    if (_user)
      this.currentUser.next(JSON.parse(_user));
    else
      this.currentUser.next(undefined);
  }

  public getCurrentUserSubject(): Subject<any> {
    return this.currentUser;
  }

  signOut(): void {
    window.sessionStorage.clear();
    this.currentUser.next(undefined);
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUser.next(user);
  }

  public getUser(): any | undefined {
    const _user = sessionStorage.getItem(USER_KEY)
    if (_user)
      return JSON.parse(_user);
    else
      return undefined
  }
}
