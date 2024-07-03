import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { Utils } from '../../utils/utils';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent implements OnInit{
  
  user: User = new User();

  usernameInvalid: boolean = false;

  emailInvalid: boolean = false;
  hasPressedSubmit: boolean = false;

  constructor(private userSevice: UserService, private authSrv: LoginService, private router: Router) {
  }
  
  ngOnInit(): void {

  }

  firstLogIn(username: string, password: string) {
    this.authSrv.login(username, password).subscribe(data => {
      this.redirectToUserList();
    });
  }

  signUpUser() {
    this.user.birthday = Utils.formatDate(this.user.birthday !== null ? this.user.birthday : '');
    this.authSrv.signup(this.user).subscribe(data => {
      console.log(data);
      this.firstLogIn(this.user.username || '', this.user.password || '');
    });
  }

  redirectToUserList() {
    this.router.navigate(['/users']);
  }

  onSubmit(userForm: any): void {
    this.hasPressedSubmit = true;
    this.userSevice.checkUsername(this.user.username).subscribe(data => {
      if (data !== null && data !== undefined)
        {this.usernameInvalid = true; console.log(this.usernameInvalid, data); this.hasPressedSubmit = false;}
      else {
        this.usernameInvalid = false;
        this.userSevice.checkEmail(this.user.email).subscribe(data => {
          if (data !== null && data !== undefined)
          {
            this.emailInvalid = true;
            this.hasPressedSubmit = false;
          }
          else {
            this.emailInvalid = false;
            if (userForm.valid && this.emailInvalid == false && this.usernameInvalid == false) {
              this.signUpUser();
              this.hasPressedSubmit = false;
            }
          }
        });
      }
    });
}
}
