import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import { Utils } from '../utils/utils';
import { urlToHttpOptions } from 'url';
import { LoginService } from './login.service';
import { group } from 'console';
import { timingSafeEqual } from 'crypto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = "http://localhost:8080/jollyday";
  constructor(private HttpClient: HttpClient, private loginSrv: LoginService) {}

  getLoginSrv() {
    return this.loginSrv;
  }

  createHeader(): HttpHeaders {
    const header = new HttpHeaders().set("Authorization", `Bearer ${this.loginSrv.getToken()}`);
    return header;
  }

  getUsersList(): Observable<User[]>{
    return this.HttpClient.get<User[]>(`${this.url}/users/all`, {headers: this.createHeader()});
  }

  addUser(user: User): Observable<Object> {
    return this.HttpClient.post(`${this.url}/auth/signup`, user);
  }

  findUserByUsername(username: string | null): Observable<User|any> {
    return this.HttpClient.get<User|any>(`${this.url}/users/${username}`, {headers: this.createHeader()});
  }

  findUserById(id: number): Observable<User> {
    return this.HttpClient.get<User>(`${this.url}/users/getUser/${id}`, {headers: this.createHeader()});
  }

  modifyUser(username: string | null, sendUsr: User): Observable<Object> {
    sendUsr.birthday = Utils.formatDate(sendUsr.birthday);
    return this.HttpClient.put(`${this.url}/users/updateUser/${username}`, sendUsr, {headers: this.createHeader()});
  }

  deleteUser(username: string | null): Observable<void> {
    return this.HttpClient.delete<void>(`${this.url}/users/deleteUser/${username}`);
  }

  getCurrentUser(tkn: string): Observable<any> {
    const header = new HttpHeaders().set("Authorization", `Bearer ${tkn}`);
    return this.HttpClient.get<any>(`${this.url}/users/getCurrentUser`, {headers: header});
  }

  participateFor(friendId: number): Observable<User[]> {
    return this.HttpClient.post<User[]>(`${this.url}/users/participatesFor/${friendId}`, {headers: this.createHeader()});
  }

  getFriends(): Observable<User[]> {
    return this.HttpClient.get<User[]>(`${this.url}/users/getFriends`, {headers: this.createHeader()});
  }

  stopParticipating(friendId: number): Observable<User[]> {
    return this.HttpClient.delete<User[]>(`${this.url}/users/stopParticipating/${friendId}`, {headers: this.createHeader()});
  }

  joinGroupRequest(groupId: number): Observable<void> {
    return this.HttpClient.put<void>(`${this.url}/users/joinGroupRequest/${groupId}`, {headers: this.createHeader()});
  }

  leaveGroupRequest(groupId: number): Observable<void> {
    return this.HttpClient.put<void>(`${this.url}/users/leaveGroup`, {headers: this.createHeader()});
  }

  acceptJoinRequest(userId: number, groupId: number): Observable<void> {
    return this.HttpClient.put<void>(`${this.url}/users/updateGroup/${groupId}/${userId}`, {headers: this.createHeader()});
  }

  excludeFromGroup(userId: number): Observable<void> {
    return this.HttpClient.put<void>(`${this.url}/users/excludeFromGroup/${userId}`, {headers: this.createHeader()});
  }

  checkUsername(username: string | null): Observable<User|any> {
    return this.HttpClient.get<User|any>(`${this.url}/auth/checkUsername/${username}`);
  }

  checkEmail(email: string | null): Observable<User|any> {
    return this.HttpClient.get<User|any>(`${this.url}/auth/checkEmail/${email}`);
  }




}
