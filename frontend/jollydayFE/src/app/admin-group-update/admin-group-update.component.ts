// src/app/update-group-admin/update-group-admin.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Group } from '../group';
import { GroupService } from '../group.service';

@Component({
  selector: 'app-admin-group-update',
  templateUrl: './admin-group-update.component.html',
  styleUrls: ['./admin-group-update.component.scss']
})
export class AdminGroupUpdateComponent implements OnInit {
  group: any = {
    id: null,
    name: '',
    description: ''
  }

  initgroup: any = {
    id: null,
    name: '',
    description: ''
  }

  groupNameInvalid: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private grpSrv: GroupService
  ) {
    const groupName = this.route.snapshot.paramMap.get('name');
    this.grpSrv.getGroup(groupName).subscribe(data => {this.group = data; this.initgroup = {id: data.id, name: data.name, description: data.description}; console.log(this.group)});

  }

  ngOnInit(): void {
  }

  onSubmit(groupForm: any): void {
    if(this.group.name.trim() === this.initgroup.name && this.group.description.trim() === this.initgroup.description) {
      console.log(this.group, this.initgroup);
      this.router.navigate(["/groups"]);
      return;
    }
    this.grpSrv.getGroup(this.group.name).subscribe(data => {
      if (data != null && data != undefined && data.id != this.group.id)
        this.groupNameInvalid = true;
      else if (groupForm.valid) {
        this.http.put(`http://localhost:8080/jollyday/groups/adminGroupUpdate/${this.group.id}`, this.group)
        .subscribe(response => {
          console.log('Group updated successfully', response);
          this.groupNameInvalid = false;
          this.router.navigate(['/groups']);
        });
      }
    });
  }

}
