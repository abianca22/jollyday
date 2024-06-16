import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { Utils } from '../../utils/utils';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.scss'
})
export class CreateUserComponent implements OnInit{

  user: User = new User();

  constructor(private userSevice: UserService, private router: Router) {
  }

  ngOnInit(): void {
  }

  addUser() {
    this.user.birthday = Utils.formatDate(this.user.birthday !== null ? this.user.birthday : '');
    this.userSevice.addUser(this.user).subscribe(data => {
      console.log(data);
      this.redirectToUserList();
    });
  }

  redirectToUserList() {
    this.router.navigate(['/users']);
  }

  onSubmit(): void {
    console.log(this.user);
    this.addUser();
  }

}
