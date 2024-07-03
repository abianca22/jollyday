// src/app/add-group/add-group.component.ts
import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { UserService } from '../user.service';
import { throwError } from 'rxjs/internal/observable/throwError';
import { catchError } from 'rxjs';
import { GroupService } from '../group.service';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.scss']
})
export class AddGroupComponent {
  group = {
    name: '',
    description: '',
    leaderId: null
  };

  groupNameInvalid = false;

  constructor(private http: HttpClient, private router: Router, private usrSrv: UserService, private grpSrv: GroupService) {}

  onSubmit(groupForm: any) {
    this.grpSrv.getGroup(this.group.name.trim()).subscribe(data => {
      if(data != null && data != undefined) {
        this.groupNameInvalid = true;
    }
      else if (groupForm.valid) {
        this.http.post('http://localhost:8080/jollyday/groups/addGroup', this.group, {headers: this.usrSrv.createHeader()}).subscribe(
          (response) => {
            console.log('Group added successfully', response);
            this.groupNameInvalid = false;
            this.router.navigate(['/groups']);
          });
    }
  });
}

  handleError(error: HttpErrorResponse) {
    if (error.status !== 200) 
      this.groupNameInvalid = true;
    return throwError(() => {});
  }
}

