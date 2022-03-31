import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Sport} from "../models/sport";
import {map, Observable, of, tap} from "rxjs";
import {Court} from "../models/court";

@Injectable({
  providedIn: 'root'
})
export class FacilityService {

  private baseUrl = 'http://localhost:8080/api/v1/facility/';

  private sports: Map<number, Sport> = new Map<number, Sport>();

  constructor(
    private http: HttpClient
  ) { }

  public getSport(id: number): Observable<Sport> {
    if (this.sports.has(id))
      return of(this.sports.get(id) as Sport);
    return this.http.get<Sport>(`${this.baseUrl}/sport`, {params: {id: id}});
  }

  public getSports(): Observable<Sport[]> {
    if (this.sports?.size > 0)
      return of(Array.from(this.sports.values()));

    return this.http.get<Sport[]>(`${this.baseUrl}/sports`).pipe(
      tap(sports => {
        sports.forEach(sport => {
          this.sports.set(sport.id, sport);
          if (sport.parent) {
            let parent = this.sports.get(sport.parent.id);
            if (!parent)
              throw new Error("Missing parent");
            sport.parent = parent;
            if (!parent.children) {
              parent.children = [sport];
            } else {
              parent.children.push(sport);
            }
          }
        })
      }),
      map(() =>  {
        return Array.from(this.sports.values()).filter(sport => !sport.parent);
      })
    );
  }

  public getCourts(): Observable<Court[]> {
    return this.http.get<Court[]>(`${this.baseUrl}/courts`);
  }

  public getCourtsForSport(sport: Sport): Observable<Court[]> {
    while (sport.parent)
      sport = sport.parent;
    return this.http.get<Court[]>(`${this.baseUrl}/courts`, {params: {sport: sport.id}});
}
}
