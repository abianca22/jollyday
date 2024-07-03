import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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
  friendNames: any[] = [];
  friends: User[] = [];


  constructor(private userService: UserService,
    private router: Router, private loginSrv: LoginService, private cdr: ChangeDetectorRef) {

    }
  
  ngOnInit(): void {
    if (this.loginSrv.getRole() == 'ADMIN') this.isAdmin = true;
    else if (this.loginSrv.getRole() == 'EDITOR') this.isEditor = true;
    this.userService.getFriends().subscribe(data => {
      this.friends = data;
      this.friendNames = this.friends.map(friend => friend.username);
    });
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
    if (this.loginSrv.getUsername() === username) return true;
    return false;
  }

  showGivenUser(username: string | null) {
    this.userService.findUserByUsername(username).subscribe(data => {
      this.router.navigate(["user", username]);
    });
  }

  stopParticipating(friendId: number | null) {
    if (friendId == null) throw new Error("Invalid userId");
    this.userService.stopParticipating(friendId).subscribe(data => {
      this.friends = data;
      this.friendNames = this.friends.map(friend => friend.username);
     });
  }

  joinFor(friendId: number | null) {
    if (friendId == null) {
      throw Error("Invalid userid");
    }
    this.userService.participateFor(friendId).subscribe(data => {
      this.friends = data;
      this.friendNames = this.friends.map(friend => friend.username);
  });
  }

  isFriend(username: string | null) {
    return username != null ? this.friendNames.includes(username) : false;
  }

  isAccepted(user: User | null) {
    if (user == null || user == undefined) return false;
    if (user.group == null || user.group == undefined) return false;
    if (user.joinStatus == "NONE" || user.joinStatus == "PENDING") return false;
    return true;
  }

}
