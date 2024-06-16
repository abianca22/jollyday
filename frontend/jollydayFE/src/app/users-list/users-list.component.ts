import { Component, OnInit } from '@angular/core';
import {User} from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.scss'
})
export class UsersListComponent implements OnInit{
  users: User[] = [];
  isAdmin: boolean = false;
  isEditor: boolean = false;

  constructor(private userService: UserService,
    private router: Router,
    private liSrv: LoginService) {
      if (liSrv.loggedInValue.userRole === 'ADMIN') this.isAdmin = true;
      else if (liSrv.loggedInValue.userRole === 'EDITOR') this.isEditor = true;
    }
  
  ngOnInit(): void {
      this.getUsers();
  }

  private getUsers() {
    this.userService.getUsersList().subscribe(data => {
      this.users = data;
    });
  }

  modifyGivenUser(username: string | null) {
    this.router.navigate(["update-user", username]);
  }

  deleteGivenUser(username: string | null) {
    this.userService.deleteUser(username).subscribe(data => {
      this.getUsers();
    });
  }

  isOwner(username: string | null): boolean {
    if (this.liSrv.loggedInValue.username === username) return true;
    return false;
  }

  showGivenUser(username: string | null) {
    this.userService.findUserByUsername(username).subscribe(data => {
      this.router.navigate(["user", username]);
    });
  }
}
