import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private baseUrl = 'http://ball.net:8080/api/v1/courses';

  constructor(private http: HttpClient) { }

  getCourse(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createCourse(course: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}` + `/create`, course);
  }

  updateCourse(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteAllCourses(): Observable<any> {
    return this.http.delete(`${this.baseUrl}` + `/delete`, { responseType: 'text' });
  }

  deleteCourse(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getCoursesList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  getCoursesBySportAndYear(sport: number, year: number, ispending: boolean): Observable<any> {
    return this.http.get(`${this.baseUrl}/sport/${sport}/year/${year}/ispending/${ispending}`);
  }
}
