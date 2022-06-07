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
    let isTennis = (sport==2 || sport==3);
    return this.http.get(`${this.baseUrlReservations}/isTennis/${isTennis}/user/${userLoggedId}`);
  }

  getReservationByDateAndSportAndHour(date: string, sport: number, hour: number): Observable<any> {
    let isTennis = (sport==2 || sport==3);
    return this.http.get(`${this.baseUrlReservations}/date/${date}/isTennis/${isTennis}/hour/${hour}`);
  }


  getReservationByDateAndSportIsTennis(date: string, isTennis: boolean): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/isTennis/${isTennis}`);
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
