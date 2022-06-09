import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of, tap} from "rxjs";
import {Match, Tournament} from "../models/tournament";
import {TournamentDefinition} from "../models/tournament-definition";
import {Serialization} from "../utilities/serialization";

@Injectable({
  providedIn: 'root'
})
export class TournamentsService {

  private baseUrl = "http://ball.net:8080/api/v1/tournaments";

  constructor(
    private http: HttpClient
  ) {
  }

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

  create(tournament: TournamentDefinition): Observable<Tournament> {
    return this.http.post<Tournament>(`${this.baseUrl}/create`, tournament)
      .pipe(
        catchError(this.handleError<Tournament>("create"))
      );
  }

  confirm(id: number): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/confirm`, {params: {id: id}}).pipe(
      // catchError(this.handleError<Tournament>("confirm"))
    );
  }

  get(id: number): Observable<Tournament> {
    console.log(`${this.baseUrl}/get?id=${id}`);
    return this.http.get<Tournament>(`${this.baseUrl}/get?id=${id}`).pipe(
      catchError(this.handleError<Tournament>("confirm"))
    );
  }

  addPlayers(tournamentID: number, ids: number[]): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/register-players`, {params: {id: tournamentID, players: ids}});
  }

  join(tournamentID: number, ids: number[]): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/join`, {params: {id: tournamentID, players: ids}});
  }

  cancel(tournamentID: number): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/cancel`, {params: {id: tournamentID}})
  }

  complete(tournamentID: number): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/close-registrations`, {params: {id: tournamentID}})
  }

  myTournaments(page: number = 0, limit: number = 5): Observable<Tournament[]> {
    console.log({params: {limit: limit, offset: page * limit}});
    return this.http.get<Tournament[]>(`${this.baseUrl}/my-tournaments`, {params: {limit: limit, offset: page * limit}});
  }

  matchResults(tournamentID: number, match: Match): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/match-results`, {
      params: {
        id: tournamentID,
        match: match.id,
        points0: match.points0,
        points1: match.points1
      }
    })
  }

  upcomingTournaments(page: number = 0, limit: number = 5): Observable<Tournament[]> {
    return this.http.get<Tournament[]>(`${this.baseUrl}/get-tournaments?`, {
      params: {
        limit: limit,
        offset: page * limit,
        statuses: ["CONFIRMED", "COMPLETED"],
        fromDate: Serialization.serializeDate(new Date())
      }
    });
  }
}
