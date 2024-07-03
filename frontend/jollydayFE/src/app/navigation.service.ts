import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from './login.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
class NavigationService {

  private url: String = "http://localhost:8080/jollyday/auth";

  constructor(private rt: Router, private loginSrv: LoginService, private http: HttpClient) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if(this.loginSrv.getToken() != undefined && this.loginSrv.getToken() != '') return true;
    else {
      this.rt.navigate(['/signin']);
      return false;
    }
  }
}

export const NavGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  return inject(NavigationService).canActivate(next, state);
}
