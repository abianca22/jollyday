import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from './event';
import { UserService } from './user.service';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private url = 'http://localhost:8080/jollyday/users';  // API-ul tău

  constructor(private http: HttpClient, private usrSrv: UserService) {}

  getEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.url}/getEvents`, {headers: this.usrSrv.createHeader()});
  }

}