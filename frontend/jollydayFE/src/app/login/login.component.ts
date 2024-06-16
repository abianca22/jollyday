import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{

  user: User = new User();
  
  constructor(private usrSrv: UserService, private authSrv: LoginService, private router: Router) {}

  ngOnInit(): void {

  }

  redirectToUserList() {
    this.router.navigate(['/users']);
  }

  logIn() {
    if(this.user.username === null || this.user.username === undefined)
      this.user.username = '';
    if(this.user.password === null || this.user.password === undefined)
      this.user.password = '';
    this.authSrv.login(this.user.username, this.user.password).subscribe(() => {
      this.redirectToUserList();
    });
  }

}
