import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';
import { Event } from '../event';
import { UserService } from '../user.service';
import { NavigationExtras, Router } from '@angular/router';
import { GroupService } from '../group.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  events: any = [];

  constructor(private eventService: EventService, private userService: UserService, private router: Router, private grpSrv: GroupService) {}

  ngOnInit(): void {
    this.eventService.getEvents().subscribe((data) => {
      this.events = data;
    });
  }

  hasGroup(place: any): boolean {
    return place !== null && place !== undefined;
  }

  redirectToUser(username: string) {
    this.router.navigate(['/user/' + username]);
  }

  redirectToGroup(name: string) {
    let id;
    this.grpSrv.getGroupId(name).subscribe(data => {
      let extras: NavigationExtras = {state: { groupId: data }}
      this.router.navigate(['/groups/' + name + "/users"], extras);
    });
  }

}
