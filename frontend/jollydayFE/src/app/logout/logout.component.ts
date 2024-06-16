import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss'
})
export class LogoutComponent implements OnInit{
  
  constructor(private loginSrv: LoginService, private router: Router) {}

  ngOnInit(): void {
  }

  logout(): void {
    this.loginSrv.logout().subscribe(data => {
      this.router.navigate(['/signin']);
    })
  }

}
