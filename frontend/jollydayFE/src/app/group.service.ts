import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginService } from './login.service';
import { Observable } from 'rxjs';
import { Group } from './group';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private url = "http://localhost:8080/jollyday/groups";

  constructor(private http: HttpClient, private loginSrv: LoginService) { }

  createHeader(): HttpHeaders {
    console.log("token: " + this.loginSrv.getToken());
    const header = new HttpHeaders().set("Authorization", `Bearer ${this.loginSrv.getToken()}`);
    return header;
  }

  getGroup(groupName: string | any) {
    return this.http.get<Group>(`${this.url}/${groupName}`);

  }

  getGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.url + "/all", {headers: this.createHeader()});
  }

  deleteGroup(groupId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/deleteGroup/${groupId}`, {headers: this.createHeader()});
  }

  getUsersFromGivenGroup(groupId: number): Observable<User[]> {
    return this.http.get<User[]>(this.url + `/${groupId}/members`, {headers: this.createHeader()});
  }

  getGroupName(groupId: number): Observable<string> {
    return this.http.get<string>(this.url + `/${groupId}/name`, {headers: this.createHeader()});
  }

  getGroupId(name: string): Observable<number> {
    return this.http.get<number>(this.url + `/${name}/id`, {headers: this.createHeader()});
  }

  changeLeaderTo(groupId: number, userId: number): Observable<void> {
    return this.http.put<void>(`${this.url}/${groupId}/updateLeaderTo/${userId}`, {headers: this.createHeader()});
  }

  removeLeader(groupId: number, userId: number): Observable<void> {
    return this.http.put<void>(`${this.url}/${groupId}/removeLeaderTitle/${userId}`, {headers: this.createHeader()});
  }
  
}
