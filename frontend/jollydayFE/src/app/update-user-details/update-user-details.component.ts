import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-user-details',
  templateUrl: './update-user-details.component.html',
  styleUrl: './update-user-details.component.scss'
})
export class UpdateUserDetailsComponent {

  user: any;
  initUser: any;



  emailInvalid: boolean = false;

  constructor(private usrSrv: UserService, private loginSrv: LoginService, private router: Router, private route: ActivatedRoute) {
    const name = this.loginSrv.getUsername();
    this.usrSrv.findUserByUsername(name).subscribe(data => {
      this.user = data;
    this.initUser = {lastName: data.lastName, firstName: data.firstName, birthday: data.birthday, email: data.email}
    let date = this.user.birthday.toString().split('.');
    this.user.birthday = `${date[2]}-${date[1]}-${date[0]}`;
    this.initUser.birthday = `${date[2]}-${date[1]}-${date[0]}`;
  });
  }

  updateUser(): void {
    this.user.lastName = this.user.lastName.trim();
    this.user.firstName = this.user.firstName.trim();
    this.usrSrv.modifyUser(this.user.username, this.user).subscribe(data => {
      this.router.navigate(["/users"]);
    })
  }

  onSubmit(userForm: any): void {
    if (this.initUser.email === this.user.email.trim() && this.initUser.firstName === this.user.firstName.trim() && this.initUser.lastName === this.user.lastName.trim() && this.initUser.birthday === this.user.birthday)
      this.router.navigate(["/users"]);
    else {
        this.usrSrv.checkEmail(this.user.email).subscribe(data => {
          if (data !== null && data !== undefined && data.id != this.user.id)
            this.emailInvalid = true;
          else {
            this.emailInvalid = false;
            if (userForm.valid) {
              this.updateUser();
            }
          }
        });
      }
}
}
