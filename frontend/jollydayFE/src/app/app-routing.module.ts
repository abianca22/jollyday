import { Inject, NgModule, inject } from '@angular/core';
import { RouterModule, Routes, mapToCanActivate, mapToCanDeactivate } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { ViewUserComponent } from './view-user/view-user.component';
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { NavGuard } from './navigation.service';
import { GroupListComponent } from './group-list/group-list.component';
import { GroupUsersListComponent } from './group-users-list/group-users-list.component';
import { EventListComponent } from './event-list/event-list.component';
import { AddGroupComponent } from './add-group/add-group.component';

const routes: Routes = [
  {
    path: 'users',
    component: UsersListComponent,
    canActivate: [NavGuard]
  },
  {
    path: 'signup',
    component: SignupComponent
  },
  {
    path: 'signin',
    component: LoginComponent
  },
  {
    path: "update-user/:username",
    component: UpdateUserComponent,
    canActivate: [NavGuard]
  },
  {
    path: "addGroup",
    component: AddGroupComponent,
    canActivate: [NavGuard]
  },
  {
    path: "user/:username",
    component: ViewUserComponent,
    canActivate: [NavGuard]
  },
  {
    path: "groups",
    component: GroupListComponent,
    canActivate: [NavGuard]
  },
  {
    path: "groups/:name/users",
    component: GroupUsersListComponent,
    canActivate: [NavGuard]
  },
  {
    path: "events",
    component: EventListComponent,
    canActivate: [NavGuard]
  },
  {
    path: '',
    redirectTo: 'signup',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
