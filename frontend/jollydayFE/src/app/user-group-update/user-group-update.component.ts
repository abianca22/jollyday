import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from '../group.service';

@Component({
  selector: 'app-user-group-update',
  templateUrl: './user-group-update.component.html',
  styleUrl: './user-group-update.component.scss'
})
export class UserGroupUpdateComponent {
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
    if(this.group.description.trim() === this.initgroup.description) {
      console.log(this.group, this.initgroup);
      this.router.navigate(["/groups"]);
      return;
    }
   
    if (groupForm.valid) {
        this.http.put(`http://localhost:8080/jollyday/groups/adminGroupUpdate/${this.group.id}`, this.group)
        .subscribe(response => {
          console.log('Group updated successfully', response);
          this.router.navigate(['/groups']);
        });
      }
  }

}
