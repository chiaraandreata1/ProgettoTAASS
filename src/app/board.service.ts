import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  private baseUrl = 'http://localhost:8080/boards';

  constructor(private http: HttpClient) { }

  getBoard(id: number): Observable<Object> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createBoard(board: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}` + `/create`, board);
  }

  updateBoard(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteBoard(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getBoardsList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  getBoardsListBySportAndType(sport: string, type: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/sport/${sport}/type/${type}`);
  }

  deleteAllBoards(): Observable<any> {
    return this.http.delete(`${this.baseUrl}` + `/delete`, { responseType: 'text' });
  }
}
