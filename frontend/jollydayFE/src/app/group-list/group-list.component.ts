import { Component, OnInit } from '@angular/core';
import { Group } from '../group';
import { GroupService } from '../group.service';
import { UserService } from '../user.service';
import { User } from '../user';
import { NavigationExtras, Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrl: './group-list.component.scss'
})
export class GroupListComponent implements OnInit{
  groups: any = [];

  userGroup: any;

  user: any;

  constructor(private groupService: GroupService, private usrService: UserService, private router: Router, private loginSrv: LoginService) {}

  ngOnInit(): void {
    this.groupService.getGroups().subscribe((data) => {
      this.groups = [];
      data.forEach(grp => {
        if (grp.leaderId != null) {
          this.usrService.findUserById(grp.leaderId).subscribe(user => {
            this.groups.push({"grp": grp, "ldr": user});
          });
        }
        else this.groups.push({"grp": grp, "ldr": null});
      });
    });

    this.usrService.findUserByUsername(this.loginSrv.getUsername()).subscribe(data => {
        this.user = data;
        if(data.groupName != "Nu face parte dintr-un grup!") {
        this.groupService.getGroupId(data.groupName).subscribe(grpId => this.userGroup = grpId);
      }
    });
  }

  sendJoinRequest(groupId: number) {
    this.usrService.joinGroupRequest(groupId).subscribe(() => {
      this.usrService.findUserByUsername(this.loginSrv.getUsername()).subscribe(data => {
        this.user = data;
        if(data.groupName != "Nu face parte dintr-un grup!") {
          this.groupService.getGroupId(data.groupName).subscribe(grpId => this.userGroup = grpId);
        }
      });
    });
  }

  hasLeader(leader: any) {
    if (leader != null && leader != undefined) return true;
    return false;
  }

  viewUsers(grpId: number, grpName: string) {
    let extras: NavigationExtras = {state: { groupId: grpId }}
    this.router.navigate([`groups/${grpName}/users`], extras);
  }

  isAlreadyMember(groupId: number) {
    return this.userGroup === groupId;
  }

  isAdmin() {
    return this.loginSrv.getRole() === 'ADMIN';
  }

  deleteGroup(groupId: number) {
    this.groupService.deleteGroup(groupId).subscribe(() => {
      this.groupService.getGroups().subscribe((data) => {
        this.groups = [];
        data.forEach(grp => {
          if (grp.leaderId != null) {
            this.usrService.findUserById(grp.leaderId).subscribe(user => {
              this.groups.push({"grp": grp, "ldr": user});
            });
          }
          else this.groups.push({"grp": grp, "ldr": null});
        });
      });
    });
  }
}
