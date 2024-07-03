import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-view-user',
  templateUrl: './view-user.component.html',
  styleUrl: './view-user.component.scss'
})
export class ViewUserComponent implements OnInit{

  username: string | null = null;
  user: any;


  constructor(private actRt: ActivatedRoute, private usrSrv: UserService, private loginSrv: LoginService, private router: Router) {
  }

  ngOnInit(): void {
    this.username = this.actRt.snapshot.params["username"];
    this.usrSrv.findUserByUsername(this.username).subscribe(data => {
      this.user = data;
      console.log(this.user);
    });
  }

  isAccepted(user: User | null) {
    if (user == null || user == undefined) return false;
    if (user.group == null || user.group == undefined) return false;
    if (user.joinStatus == "NONE" || user.joinStatus == "PENDING") return false;
    return true;
  }

  isOwner() {
    return this.username === this.loginSrv.getUsername();
  }

  deleteAccount() {
    this.usrSrv.deleteUser(this.loginSrv.getUsername()).subscribe(() => {
      this.loginSrv.deleteCookies();
      this.router.navigate(["/signup"]);
  });
}
}
