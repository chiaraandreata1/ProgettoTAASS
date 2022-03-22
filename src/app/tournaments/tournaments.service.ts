import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {TournamentBuilding} from "../models/tournament-building";
import {catchError, Observable, of, tap} from "rxjs";
import {Tournament} from "../models/tournament";

@Injectable({
  providedIn: 'root'
})
export class TournamentsService {

  private baseUrl = "http://localhost:8080/api/v1/tournaments";

  constructor(
    private http: HttpClient
  ) { }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  createTournament(tournament: TournamentBuilding): Observable<Tournament> {
    return this.http.post<Tournament>(`${this.baseUrl}/create`, tournament).pipe(
      catchError(this.handleError<Tournament>("create"))
    );
  }

  confirm(tournament: Tournament): Observable<Tournament> {
    let body = Tournament.toJSON(tournament);
    return this.http.post<Tournament>(`${this.baseUrl}/confirm`, body).pipe(
      catchError(this.handleError<Tournament>("confirm"))
    );
  }

  get(id: number): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/${id}`).pipe(
      catchError(this.handleError<Tournament>("confirm"))
    );
  }
}
