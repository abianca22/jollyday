import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService{

  private url = 'http://localhost:8080/jollyday/auth';
  private loggedInSubject: BehaviorSubject<any>;
  public  loggedIn: Observable<any>;


  constructor(private http: HttpClient) {
    this.loggedInSubject = new BehaviorSubject<any>(JSON.stringify({jwtToken: "token expirat", userRole: null}));
    this.loggedIn = this.loggedInSubject.asObservable();
    this.http.get<string>(`${this.url}/loggedInUser`).subscribe(data => this.loggedInSubject.next(data));
  }

  public get loggedInValue(): any {
      return this.loggedInSubject.value;
  }

  public loggedInSubjectSetter(loggedInSubj: any): void {
    this.loggedInSubject.next(loggedInSubj);
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.url}/signin`, {username, password}).pipe(map(user => {
      this.loggedInSubject.next(user);
      return user;
    }));
  }

  logout(): Observable<any> {
    let tkn = this.loggedInValue?.jwtToken;
    if (!tkn) {
      throw new Error('Nu s-a gasit niciun token');
    }
    let headers = new HttpHeaders().set("Authorization", `Bearer ${tkn}`);
    return this.http.post<any>(`${this.url}/logout`, {}, { headers }).pipe(map(usr => {
      this.loggedInSubject.next(new BehaviorSubject<any>(JSON.stringify({jwtToken: "token expirat", userRole: null})));
      return usr;
    }));
  }

}
