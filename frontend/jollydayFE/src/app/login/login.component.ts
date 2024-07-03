import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{

  user: User = new User();

  failedLogin: boolean = false;
  
  constructor(private authSrv: LoginService, private router: Router, private cookieService: CookieService, private usrSrv: UserService) {}

  ngOnInit(): void {

  }

  redirectToUserList() {
    this.router.navigate(['/users']);
  }

  logIn(userForm: any) {
    if(this.user.username === null || this.user.username === undefined)
      this.user.username = '';
    if(this.user.password === null || this.user.password === undefined)
      this.user.password = '';
    this.authSrv.login(this.user.username, this.user.password).subscribe(data => {
      if(data.jwtToken === null || data.jwtToken === undefined) {
        this.failedLogin = true;
      }
      else if (userForm.valid) {
        this.cookieService.set("jwt", data.jwtToken, 1);
        this.usrSrv.getCurrentUser(this.cookieService.get("jwt")).subscribe(user => {
          this.cookieService.set("currentUserRole", user.userRole, 1);
          this.cookieService.set("currentUsername", user.username, 1);
          if (this.user.password != '' && this.user.username != '')
            this.redirectToUserList();
      });
    }
  });
  }

}
