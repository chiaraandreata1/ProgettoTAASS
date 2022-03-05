import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrlReservations = 'http://localhost:8080/reservations';
  private baseUrlCourts = 'http://localhost:8080/courts';

  constructor(private http: HttpClient) { }

  getReservation(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrlReservations}/${id}`);
  }

  getReservationByDateAndSport(date: string, sport: string): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}`);
  }

  createReservation(reservation: Object): Observable<Object> {
    return this.http.post(`${this.baseUrlReservations}` + `/create`, reservation);
  }

  updateReservation(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrlReservations}/${id}`, value);
  }

  deleteReservation(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}/${id}`, { responseType: 'text' });
  }

  getReservationsList(): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}`);
  }

  getCourtsList(): Observable<any> {
    return this.http.get(`${this.baseUrlCourts}`)
  }

  deleteAllReservations(): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}` + `/delete`, { responseType: 'text' });
  }
}
