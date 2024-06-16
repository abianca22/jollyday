import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Utils } from '../../utils/utils';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrl: './update-user.component.scss'
})
export class UpdateUserComponent implements OnInit{
  
  user: User = new User();
  username: string | null = null;
  constructor(private usrSrv: UserService, private actRt: ActivatedRoute, private router: Router) {

  }
  
  ngOnInit(): void {
    this.username = this.actRt.snapshot.params['username'];
    this.usrSrv.findUserByUsername(this.username).subscribe(data => {
      let dataInterm =  data;
      dataInterm.birthday = data.birthday !== null ? Utils.deformatDate(data.birthday) : null;
      this.user = dataInterm;
    });
  }


  onSubmit() {
    this.usrSrv.modifyUser(this.username, this.user).subscribe(data => {
      this.redirectToUserList();
    })
  }

  
  redirectToUserList() {
    this.router.navigate(['/users']);
  }
}
