import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from 'rxjs';

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

  public getCurrentUser(): Observable<any | undefined> {

    let observable: Observable<any>;
    if (this.currentUser)
      observable = of(this.currentUser);
    else
      observable = this.http.get(`${this.baseUrl}/me`);
    return observable;
  }

}
