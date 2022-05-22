import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrlReservations = 'http://localhost:8080/api/v1/reservations';
  private baseUrlCourts = 'http://localhost:8080/api/v1/reservations/courts';

  constructor(private http: HttpClient) { }


  getUserLoggedReservationsBySport(userLoggedId: number, sport: String): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/sportuser/${sport}/user/${userLoggedId}`);
  }

  getReservationByDateAndSport(date: string, sport: string): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}`);
  }

  createReservation(reservation: Object): Observable<Object> {
    return this.http.post(`${this.baseUrlReservations}` + `/create`, reservation);
  }

  deleteReservation(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}/${id}`, { responseType: 'text' });
  }

  getReservationsList(): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}`);
  }

  getCourtsListByType(sport: string): Observable<any> {
    return this.http.get(`${this.baseUrlCourts}/sport/${sport}`)
  }

  deleteAllReservations(): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}` + `/delete`, { responseType: 'text' });
  }
}
