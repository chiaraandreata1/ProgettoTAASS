import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrlReservations = 'http://ball.net:8080/api/v1/reservations';
  private baseUrlCourts = 'http://ball.net:8080/api/v1/reservations/courts';

  constructor(private http: HttpClient) { }


  getUserLoggedReservationsBySport(userLoggedId: number, sport: number): Observable<any> {
    let isTennis = sport==2 ? true : false;
    return this.http.get(`${this.baseUrlReservations}/isTennis/${isTennis}/user/${userLoggedId}`);
  }

  getReservationByDateAndSportAndHour(date: string, sport: number, hour: number): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}/hour/${hour}`);
  }

  getReservationByDateAndSport(date: string, sport: number): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}/date/${date}/sport/${sport}`);
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

  getReservationsList(): Observable<any> {
    return this.http.get(`${this.baseUrlReservations}`);
  }

  getCourtsListByType(sport: string): Observable<any> {
    return this.http.get(`${this.baseUrlCourts}/sport/${sport}`)
  }

  deleteAllReservations(): Observable<any> {
    return this.http.delete(`${this.baseUrlReservations}` + `/delete`, { responseType: 'text' });
  }

  getReservationsByIds(ids: number[]){
    let stringListIds = '';
    for (let i = 0; i<ids.length;i++)
      if (i+1<ids.length)  stringListIds+=ids[i]+','
      else stringListIds+=ids[i]
    return this.http.get(`${this.baseUrlReservations}/getByIds?Ids=`+stringListIds)
  }
}
