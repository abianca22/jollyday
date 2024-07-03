import { Component, OnInit } from '@angular/core';
import { GroupService } from '../group.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-group-users-list',
  templateUrl: './group-users-list.component.html',
  styleUrl: './group-users-list.component.scss'
})
export class GroupUsersListComponent {
  
  users: any = [];
  groupId: number;
  groupName: any;
  group: any;

  constructor(private grpSrv: GroupService, private router: Router, private actRt: ActivatedRoute, private loginSrv: LoginService, private usrSrv: UserService) {
    this.groupId = window.history.state.groupId;
    this.groupName = this.actRt.snapshot.params["name"];
    grpSrv.getGroup(this.groupName).subscribe(data => this.group = data);
    grpSrv.getUsersFromGivenGroup(this.groupId).subscribe(data => {this.users = data; console.log(data)});
  }

  redirectToUser(username: string) {
    this.router.navigate(['/user/' + username]);
  }

  isAdmin(): boolean {
    return this.loginSrv.getRole() === 'ADMIN';
  }

  acceptRequest(groupId: number, userId: number) {
    this.usrSrv.acceptJoinRequest(userId, groupId).subscribe(() => {
      this.grpSrv.getUsersFromGivenGroup(this.groupId).subscribe(data => {this.users = data; console.log(data)});
      this.grpSrv.getGroup(this.groupName).subscribe(data => this.group = data);

    });
  }

  excludeRequest(userId: number) {
    this.usrSrv.excludeFromGroup(userId).subscribe(() => {
      this.grpSrv.getUsersFromGivenGroup(this.groupId).subscribe(data => {this.users = data; console.log(data)});
      this.grpSrv.getGroup(this.groupName).subscribe(data => this.group = data);
    });
  }

  changeLeader(groupId: number, userId: number) {
    this.grpSrv.changeLeaderTo(groupId, userId).subscribe((data) => {
      if (data !== null && data !== undefined) {
      this.grpSrv.getUsersFromGivenGroup(this.groupId).subscribe(users => {this.users = users; });
      this.grpSrv.getGroup(this.groupName).subscribe(data => this.group = data);
      }
      else throw new Error("Group not correctly fetched");
    });
  }

  removeLeader(groupId: number, userId: number) {
    this.grpSrv.removeLeader(groupId, userId).subscribe((data) => {
      if (data !== null && data !== undefined) {
      this.grpSrv.getUsersFromGivenGroup(this.groupId).subscribe(users => {this.users = users; });
      this.grpSrv.getGroup(this.groupName).subscribe(data => this.group = data);
      }
      else throw new Error("Group not correctly fetched");
    });
  }

  isOwner(username: string | null): boolean {
    if (this.loginSrv.getUsername() === username) return true;
    return false;
  }

  isLeader(leaderId: number, userId: number) {
    return leaderId === userId;
  }

  leaveGroup(groupId: number) {
    this.usrSrv.leaveGroupRequest(groupId).subscribe(data => {
      this.router.navigate(["/groups"]);
    });
  }
  

}
