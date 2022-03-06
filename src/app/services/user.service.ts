import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrlUsers = 'http://localhost:8080/users';

  constructor(private http: HttpClient) { }

  getUser(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrlUsers}/${id}`);
  }

  getUsersList(): Observable<any> {
    return this.http.get(`${this.baseUrlUsers}`);
  }

}
