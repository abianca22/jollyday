import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { User } from './user';
import { unwatchFile } from 'fs';

@Injectable({
  providedIn: 'root'
})
export class LoginService{

  private url = 'http://localhost:8080/jollyday/auth';

  public d: any;

  constructor(private http: HttpClient, private cookieService: CookieService) {

  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.url}/signin`, {username, password}).pipe(map(user => {
      return user;
    }));
  }

  getRole(): string {
    return this.cookieService.get("currentUserRole");
  }

  getUsername(): string {
    return this.cookieService.get("currentUsername");
  }

  logout(): Observable<any> {
    let tkn = this.getToken();
    if (tkn == undefined || tkn == '') {
      throw new Error('Nu s-a gasit niciun token');
    }
    let headers = new HttpHeaders().set("Authorization", `Bearer ${tkn}`);
    this.cookieService.delete('jwt', '/');
    this.cookieService.delete('currentUserRole', '/');
    this.cookieService.delete('currentUsername', '/');
    return this.http.post<any>(`${this.url}/logout`, {}, { headers }).pipe(map(usr => {
      return usr;
    }));
  }

  signup(user: User): Observable<Object> {
    return this.http.post(`${this.url}/signup`, user);
  }

  getToken(): string {
    return this.cookieService.get('jwt');
  }

}
