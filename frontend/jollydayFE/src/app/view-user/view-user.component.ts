import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-view-user',
  templateUrl: './view-user.component.html',
  styleUrl: './view-user.component.scss'
})
export class ViewUserComponent implements OnInit{

  username: string | null = null;
  user: User = new User();

  constructor(private actRt: ActivatedRoute, private usrSrv: UserService) {}

  ngOnInit(): void {
    this.username = this.actRt.snapshot.params["username"];
    this.usrSrv.findUserByUsername(this.username).subscribe(data => {
      this.user = data;
    });
  }
}
