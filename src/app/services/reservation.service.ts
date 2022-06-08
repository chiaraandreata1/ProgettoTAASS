import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrlReservations = 'http://ball.net:8080/api/v1/reservations';

  constructor(private http: HttpClient) { }

  getUserLoggedReservationsBySport(userLoggedId: number, sport: number): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/sport/${sport}/user/${userLoggedId}`);
  }

  getReservationByDateAndSportAndHour(date: string, sport: number, hour: number): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}/hour/${hour}`);
  }


  getReservationByDateAndSport(date: string, sport: number): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}`);
  }

  createReservation(reservation: Object): Observable<Object> {
    return this.http.post(`${this.baseUrlReservations}` + `/create`, reservation);
  }

  deleteReservation(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}/${id}`, { responseType: 'text' });
  }

  getReservationsByIds(ids: number[]){
    let stringListIds = '';
    for (let i = 0; i<ids.length;i++)
      if (i+1<ids.length)  stringListIds+=ids[i]+','
      else stringListIds+=ids[i]
    return this.http.get(`${this.baseUrlReservations}/getByIds?Ids=`+stringListIds)
  }
}
