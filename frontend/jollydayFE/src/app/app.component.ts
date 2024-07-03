import { Component, OnInit } from '@angular/core';
import { LoginService } from './login.service';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  constructor(private loginService: LoginService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
       if(this.router.url === '/signin' || this.router.url === '/signup')
        this.isLoggedIn = false;
       else
       this.isLoggedIn = true;
    });
  }

  title = 'JOLLYDAY';
}
