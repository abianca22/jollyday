import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import { Utils } from '../utils/utils';
import { urlToHttpOptions } from 'url';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = "http://localhost:8080/jollyday";
  constructor(private HttpClient: HttpClient, private loginSrv: LoginService) {}

  createHeader(): HttpHeaders {
    const header = new HttpHeaders().set("Authorization", `Bearer ${this.loginSrv.loggedInValue.jwtToken}`);
    return header;
  }

  getUsersList(): Observable<User[]>{
    return this.HttpClient.get<User[]>(`${this.url}/users/all`, {headers: this.createHeader()});
  }

  addUser(user: User): Observable<Object> {
    return this.HttpClient.post(`${this.url}/auth/signup`, user);
  }

  findUserByUsername(username: string | null): Observable<User> {
    return this.HttpClient.get<User>(`${this.url}/users/${username}`, {headers: this.createHeader()});
  }

  modifyUser(username: string | null, sendUsr: User): Observable<Object> {
    sendUsr.birthday = Utils.formatDate(sendUsr.birthday);
    return this.HttpClient.put(`${this.url}/updateUser/${username}`, sendUsr, {headers: this.createHeader()});
  }

  deleteUser(username: string | null): Observable<Object> {
    return this.HttpClient.delete(`${this.url}/users/${username}`);
  }

}
